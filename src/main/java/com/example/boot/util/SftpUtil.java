package com.example.boot.util;


import com.example.boot.bean.SftpConfig;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ljx
 * @version 1.0.0
 * @create 2022/8/5 15:32
 */
@Component
public class SftpUtil {


    private static final Logger logger = LoggerFactory.getLogger(SftpUtil.class);
    private static final List<String> PICTURE = Arrays.asList(".png", ".jpg", ".jpeg");
    private static final List<String> AUDIO = Arrays.asList(".mp3");
    private static final List<String> VIDEO = Arrays.asList(".mp4", ".avi", ".wmv");
    public static ThreadLocal<SftpUtil> THREAD_LOCAL = new ThreadLocal<>();
    @Resource
    private SftpConfig sftpConfig;
    private ChannelSftp channelSftp;
    private Session session;
    private ChannelExec channelExec;
    private Session sessionExec;

    /**
     * 判断图片，音频，视频
     *
     * @param suffix 后缀
     * @return string
     */
    public static String getFileType(String suffix) {
        if (PICTURE.contains(suffix)) {
            return "0";
        }
        if (AUDIO.contains(suffix)) {
            return "1";
        }
        if (VIDEO.contains(suffix)) {
            return "2";
        }
        return null;
    }

    public SftpConfig getSftpConfig() {
        return sftpConfig;
    }

    /**
     * 连接sftp服务器
     */
    public void connect() throws Exception {
        SftpUtil sftpUtil = new SftpUtil();
        sftpUtil.sftpConfig = sftpConfig;
        Integer port = sftpConfig.getPort();
        String name = sftpConfig.getHostname();
        String host = sftpConfig.getHost();
        JSch jSch = new JSch();
        logger.info("连接ftp主机，ip：{}:{}", sftpConfig.getHost(), sftpConfig.getPort());
        try {
            if (port <= 0) {
                // 连接服务器，默认端口
                sftpUtil.session = jSch.getSession(name, host);
            } else {
                sftpUtil.session = jSch.getSession(name, host, port);
            }
            if (sftpUtil.session == null) {
                throw new Exception("session is null");
            }
            // 设置登录主机的密码
            sftpUtil.session.setPassword(sftpConfig.getPassword());
            // 设置第一次登录的时候提示,不检查密钥 可选值：(ask | yes | no)
            sftpUtil.session.setConfig("StrictHostKeyChecking", "no");
            // 设置登录超时时间
            sftpUtil.session.connect(sftpConfig.getTimeout());
            // 创建sftp通信通道
            Channel channel = sftpUtil.session.openChannel("sftp");
            channel.connect(30000);
            sftpUtil.channelSftp = (ChannelSftp) channel;
//			//进入默认目录
//			sftpUtil.channelSftp.cd(sftpConfig.getPath());
            THREAD_LOCAL.set(sftpUtil);
            logger.info("SFTP服务器登录成功！ip：{}:{}", sftpConfig.getHost(), sftpConfig.getPort());
        } catch (Exception e) {
            logger.info("SFTP服务器登录失败！ip：{}:{}", sftpConfig.getHost(), sftpConfig.getPort());
            e.printStackTrace();
            throw new Exception("SFTP服务器连接失败：{}", e);
        }
    }

    /**
     * 连接sftp服务器执行shell
     */
    public boolean execShell(String command) {
        InputStream in = null;
        SftpUtil sftpUtil = new SftpUtil();
        sftpUtil.sftpConfig = sftpConfig;
        Integer port = sftpConfig.getPort();
        String name = sftpConfig.getHostname();
        String host = sftpConfig.getHost();
        JSch jSch = new JSch();
        logger.info("连接ftp主机，ip：{}:{}", sftpConfig.getHost(), sftpConfig.getPort());
        try {
            if (port <= 0) {
                // 连接服务器，默认端口
                sftpUtil.sessionExec = jSch.getSession(name, host);
            } else {
                sftpUtil.sessionExec = jSch.getSession(name, host, port);
            }
            if (sftpUtil.sessionExec == null) {
                throw new Exception("sessionExec is null");
            }
            // 设置登录主机的密码
            sftpUtil.sessionExec.setPassword(sftpConfig.getPassword());
            // 设置第一次登录的时候提示,不检查密钥 可选值：(ask | yes | no)
            sftpUtil.sessionExec.setConfig("StrictHostKeyChecking", "no");
            // 设置登录超时时间
            sftpUtil.sessionExec.connect(sftpConfig.getTimeout());
            // 创建sftp通信通道
            sftpUtil.channelExec = (ChannelExec) sftpUtil.sessionExec.openChannel("exec");
            in = sftpUtil.channelExec.getInputStream();
            sftpUtil.channelExec.setCommand(command);
            sftpUtil.channelExec.connect();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    logger.info(new String(tmp, 0, i));
                }
                if (sftpUtil.channelExec.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    //获取shell命令退出状态  0为成功
                    logger.info("exit-status: " + sftpUtil.channelExec.getExitStatus());
                    if (sftpUtil.channelExec.getExitStatus() != 0) {
                        return false;
                    }
                    break;
                }
            }
            logger.info("SHELL命令执行成功！{}", command);
            return true;
        } catch (Exception e) {
            logger.info("SHELL命令执行失败！{}", command);
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sftpUtil.channelExec.disconnect();
            sftpUtil.sessionExec.disconnect();
        }
    }

    /**
     * 关闭连接
     */
    public void disConnect() {
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        if (sftpUtil.channelSftp != null && sftpUtil.channelSftp.isConnected()) {
            sftpUtil.channelSftp.disconnect();
        }
        if (sftpUtil.session != null && sftpUtil.session.isConnected()) {
            sftpUtil.session.disconnect();
        }
    }

    /**
     * 批量上传文件
     */
    public String batchUploadFile(String path, String fileName, MultipartFile[] files) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try {
            if (!StringUtils.isEmpty(path)) {
                path = splicingPath(sftpConfig.getPath(), path);
//				if (batchRemoveFile(path)) {
                try {
                    sftpUtil.channelSftp.cd(path);
                } catch (SftpException e) {
                    try {
                        sftpUtil.channelSftp.mkdir(path);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                logger.info("SFTP上传路径：{}", path);
//				}
                for (MultipartFile file : files) {
                    try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
                        sftpUtil.channelSftp.put(inputStream, StringUtils.isEmpty(fileName) ? file.getOriginalFilename() : fileName);
                    } catch (Exception e) {
                        logger.info("SFTP批量上传异常!");
                        throw new RuntimeException("SFTP批量上传异常:", e);
                    }
                }
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return path;
        } catch (Exception e) {
            logger.info("SFTP批量上传异常!");
            throw new RuntimeException("SFTP批量上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 批量上传文件
     */
    public List<String> batchUploadFile(MultipartFile[] files, String path, List<String> fileNames) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        List<String> paths = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(path)) {
                path = splicingPath(sftpConfig.getPath(), path);
//				if (batchRemoveFile(path)) {
                try {
                    sftpUtil.channelSftp.cd(path);
                } catch (SftpException e) {
                    try {
                        sftpUtil.channelSftp.mkdir(path);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                logger.info("SFTP上传路径：{}", path);
//				}
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
                        sftpUtil.channelSftp.put(inputStream, fileNames.get(i));
                        String thispath = path + "/" + fileNames.get(i);
                        paths.add(thispath);
                    } catch (Exception e) {
                        logger.info("SFTP批量上传异常!");
                        throw new RuntimeException("SFTP批量上传异常:", e);
                    }
                }
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return paths;
        } catch (Exception e) {
            logger.info("SFTP批量上传异常!");
            throw new RuntimeException("SFTP批量上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 批量上传文件
     */
    public List<String> batchUploadFile(MultipartFile[] files, String path1, String path2) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        List<String> paths = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(path1)) {
                path1 = splicingPath(sftpConfig.getPath(), path1);
                try {
                    sftpUtil.channelSftp.cd(path1);
                } catch (SftpException e) {
                    try {
                        sftpUtil.channelSftp.mkdir(path1);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path1);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path1);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                if (!StringUtils.isEmpty(path2)) {
                    path2 = splicingPath(path1, path2);
                    try {
                        sftpUtil.channelSftp.cd(path2);
                    } catch (SftpException e) {
                        try {
                            sftpUtil.channelSftp.mkdir(path2);
                            // 进入指定目录
                            sftpUtil.channelSftp.cd(path2);
                        } catch (SftpException e1) {
                            logger.info("SFTP创建文件路径失败!path:{}", path2);
                            throw new RuntimeException("SFTP创建文件路径失败:", e1);
                        }
                    }
                    for (MultipartFile file : files) {
                        String originalFilename = file.getOriginalFilename();
                        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
                            sftpUtil.channelSftp.put(inputStream, originalFilename);
                            paths.add(path2 + "/" + originalFilename);
                        } catch (Exception e) {
                            logger.info("SFTP批量上传异常!{}:上传失败！", originalFilename);
                        }
                    }
                }
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return paths;
        } catch (Exception e) {
            logger.info("SFTP批量上传异常!");
            throw new RuntimeException("SFTP批量上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 批量上传文件
     */
    public List<String> batchUploadFile(MultipartFile[] files, String path1, String path2, String path3) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        List<String> paths = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(path1)) {
                path1 = splicingPath(sftpConfig.getPath(), path1);
                try {
                    sftpUtil.channelSftp.cd(path1);
                } catch (SftpException e) {
                    try {
                        sftpUtil.channelSftp.mkdir(path1);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path1);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path1);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                if (!StringUtils.isEmpty(path2) && !StringUtils.isEmpty(path3)) {
                    path2 = splicingPath(path1, path2);
                    try {
                        sftpUtil.channelSftp.cd(path2);
                    } catch (SftpException e) {
                        try {
                            sftpUtil.channelSftp.mkdir(path2);
                            // 进入指定目录
                            sftpUtil.channelSftp.cd(path2);
                        } catch (SftpException e1) {
                            logger.info("SFTP创建文件路径失败!path:{}", path2);
                            throw new RuntimeException("SFTP创建文件路径失败:", e1);
                        }
                    }
                    path3 = splicingPath(path2, path3);
                    try {
                        sftpUtil.channelSftp.cd(path3);
                    } catch (SftpException e) {
                        try {
                            sftpUtil.channelSftp.mkdir(path3);
                            // 进入指定目录
                            sftpUtil.channelSftp.cd(path3);
                        } catch (SftpException e1) {
                            logger.info("SFTP创建文件路径失败!path:{}", path3);
                            throw new RuntimeException("SFTP创建文件路径失败:", e1);
                        }
                    }
                    for (MultipartFile file : files) {
                        String originalFilename = file.getOriginalFilename();
                        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
                            sftpUtil.channelSftp.put(inputStream, originalFilename);
                            paths.add(path3 + "/" + originalFilename);
                        } catch (Exception e) {
                            logger.info("SFTP批量上传异常!{}:上传失败！", originalFilename);
                        }
                    }
                }
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return paths;
        } catch (Exception e) {
            logger.info("SFTP批量上传异常!");
            throw new RuntimeException("SFTP批量上传异常:", e);
        } finally {
            disConnect();
        }
    }


    /**
     * 批量上传文件
     */
    public List<Map<String, String>> batchUploadFile(MultipartFile[] files, String path1, String path2, String path3, String filenamePrefix, int index) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        List<Map<String, String>> paths = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(path1)) {
                path1 = splicingPath(sftpConfig.getPath(), path1);
                try {
                    sftpUtil.channelSftp.cd(path1);
                } catch (SftpException e) {
                    try {
                        sftpUtil.channelSftp.mkdir(path1);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path1);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path1);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                if (!StringUtils.isEmpty(path2)) {
                    path2 = splicingPath(path1, path2);
                    try {
                        sftpUtil.channelSftp.cd(path2);
                    } catch (SftpException e) {
                        try {
                            sftpUtil.channelSftp.mkdir(path2);
                            // 进入指定目录
                            sftpUtil.channelSftp.cd(path2);
                        } catch (SftpException e1) {
                            logger.info("SFTP创建文件路径失败!path:{}", path2);
                            throw new RuntimeException("SFTP创建文件路径失败:", e1);
                        }
                    }
                    if (!StringUtils.isEmpty(path3)) {
                        path3 = splicingPath(path2, path3);
                        try {
                            sftpUtil.channelSftp.cd(path3);
                        } catch (SftpException e) {
                            try {
                                sftpUtil.channelSftp.mkdir(path3);
                                // 进入指定目录
                                sftpUtil.channelSftp.cd(path3);
                            } catch (SftpException e1) {
                                logger.info("SFTP创建文件路径失败!path:{}", path3);
                                throw new RuntimeException("SFTP创建文件路径失败:", e1);
                            }
                        }
                        for (int i = 0; i < files.length; i++) {
                            MultipartFile file = files[i];
                            try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
                                String originalFilename = file.getOriginalFilename();
                                String id = StringUtils.getUuid32();
                                String suffix = null;
                                if (originalFilename != null) {
                                    suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                                }
                                String filename = filenamePrefix + "-" + (i + 1 + index) + suffix;
                                String filepath = id + suffix;
                                sftpUtil.channelSftp.put(inputStream, filepath);
                                Map<String, String> map = new HashMap<>();
                                map.put("id", id);
                                map.put("filename", filename);
                                map.put("filepath", path3 + "/" + filepath);
                                map.put("suffix", suffix);
                                paths.add(map);
                            } catch (Exception e) {
                                logger.info("SFTP批量上传异常!");
                                throw new RuntimeException("SFTP批量上传异常:", e);
                            }
                        }
                    }
                }
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return paths;
        } catch (Exception e) {
            logger.info("SFTP批量上传异常!");
            throw e;
        } finally {
            disConnect();
        }
    }

    /**
     * 上传文件
     *
     * @param path
     * @param fileName
     * @param inputStream
     * @return
     *
     * @throws Exception
     */
    public String uploadStream(String path, String fileName, InputStream inputStream) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try {
            if (!StringUtils.isEmpty(path)) {
                path = splicingPath(sftpConfig.getPath(), path);
                if (verifyDirectoryExists(path)) {
                    sftpUtil.channelSftp.cd(path);
                } else {
                    try {
                        sftpUtil.channelSftp.mkdir(path);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                sftpUtil.channelSftp.put(inputStream, fileName);
                logger.info("SFTP上传成功!文件名：{}", fileName);
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return path + "/" + fileName;
        } catch (Exception e) {
            logger.info("SFTP上传异常!");
            throw new RuntimeException("SFTP上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 上传文件
     *
     * @param path
     * @param fileName
     * @param bytes
     * @return
     *
     * @throws Exception
     */
    public String uploadStream(String path, String fileName, byte[] bytes) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            if (!StringUtils.isEmpty(path)) {
                path = splicingPath(sftpConfig.getPath(), path);
                if (verifyDirectoryExists(path)) {
                    sftpUtil.channelSftp.cd(path);
                } else {
                    try {
                        sftpUtil.channelSftp.mkdir(path);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                sftpUtil.channelSftp.put(byteArrayInputStream, fileName);
                logger.info("SFTP上传成功!文件名：{}", fileName);
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return path + "/" + fileName;
        } catch (Exception e) {
            logger.info("SFTP上传异常!");
            throw new RuntimeException("SFTP上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 上传文件
     *
     * @param fileName
     * @param bytes
     * @return
     *
     * @throws Exception
     */
    public String uploadStream(String fileName, byte[] bytes) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        String path = sftpConfig.getPath();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            sftpUtil.channelSftp.cd(path);
            sftpUtil.channelSftp.put(byteArrayInputStream, fileName);
            logger.info("SFTP上传成功!文件名：{}", fileName);
            return path + "/" + fileName;
        } catch (Exception e) {
            logger.info("SFTP上传异常!");
            throw new RuntimeException("SFTP上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 上传文件
     *
     * @param path
     * @param fileName
     * @param file
     * @return
     *
     * @throws Exception
     */
    public String uploadFile(String path, String fileName, MultipartFile file) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes())) {
            if (!StringUtils.isEmpty(path)) {
                path = splicingPath(sftpConfig.getPath(), path);
                if (verifyDirectoryExists(path)) {
                    sftpUtil.channelSftp.cd(path);
                } else {
                    try {
                        sftpUtil.channelSftp.mkdir(path);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                sftpUtil.channelSftp.put(byteArrayInputStream, fileName);
                logger.info("SFTP上传成功!文件名：{}", fileName);
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return path;
        } catch (Exception e) {
            logger.info("SFTP上传异常!");
            throw new RuntimeException("SFTP上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 上传zip文件
     *
     * @param path
     * @param fileName
     * @param files
     * @return
     *
     * @throws Exception
     */
    public String uploadZipFile(String path, String fileName, MultipartFile[] files) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try {
            if (!StringUtils.isEmpty(path)) {
                path = splicingPath(sftpConfig.getPath(), path);
                try {
                    sftpUtil.channelSftp.cd(path);
                } catch (SftpException e) {
                    try {
                        sftpUtil.channelSftp.mkdir(path);
                        // 进入指定目录
                        sftpUtil.channelSftp.cd(path);
                    } catch (SftpException e1) {
                        logger.info("SFTP创建文件路径失败!path:{}", path);
                        throw new RuntimeException("SFTP创建文件路径失败:", e1);
                    }
                }
                logger.info("SFTP上传路径：{}", path);
                try (InputStream inputStream = ZipUtil.zip(files, fileName)) {
                    sftpUtil.channelSftp.put(inputStream, fileName);
                } catch (Exception e) {
                    logger.info("SFTP批量上传异常!");
                    throw new RuntimeException("SFTP批量上传异常:", e);
                }
            } else {
                throw new RuntimeException("路径不能为空");
            }
            return path;
        } catch (Exception e) {
            logger.info("SFTP批量上传异常!");
            throw new RuntimeException("SFTP批量上传异常:", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 批量下载文件
     *
     * @param path 下载路径
     */
    public List<OutputStream> batchDownloadFile(String path) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        List<OutputStream> list = new ArrayList<>();
        try {
            if (!StringUtils.isEmpty(path)) {
                path = splicingPath(sftpConfig.getPath(), path);
                if (verifyDirectoryExists(path)) {
                    Vector<ChannelSftp.LsEntry> files = sftpUtil.channelSftp.ls(path);
                    sftpUtil.channelSftp.cd(path);
                    for (ChannelSftp.LsEntry file : files) {
                        String filename = file.getFilename();
                        if (!filename.startsWith(".")) {
                            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
                                sftpUtil.channelSftp.get(filename, outputStream);
                                list.add(outputStream);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info("SFTP批量下载失败!异常：{}", e.getMessage());
            throw new RuntimeException("SFTP批量上传异常：", e);
        } finally {
            // 关闭连接
            disConnect();
        }
        return list;
    }

    public ByteArrayOutputStream batchDownloadFile(List<String> paths, List<String> names) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);) {
                for (int i = 0; i < paths.size(); i++) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
                        sftpUtil.channelSftp.get(paths.get(i), outputStream);
                        zipOutputStream.putNextEntry(new ZipEntry(names.get(i)));
                        zipOutputStream.write(outputStream.toByteArray());
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                zipOutputStream.close();
                return byteArrayOutputStream;
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("创建zip文件失败");
            }
        } catch (Exception e) {
            logger.info("SFTP批量下载失败!异常：{}", e.getMessage());
            throw new RuntimeException("SFTP批量上传异常：", e);
        } finally {
            // 关闭连接
            disConnect();
        }
    }


    public ByteArrayOutputStream batchDownloadBbFile(List<String> paths, List<String> names) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try {
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);) {
                for (int i = 0; i < paths.size(); i++) {
                    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
                        sftpUtil.channelSftp.cd(sftpConfig.getPath() + "/" + paths.get(i));
                        sftpUtil.channelSftp.get(names.get(i), outputStream);
                        zipOutputStream.putNextEntry(new ZipEntry(names.get(i)));
                        zipOutputStream.write(outputStream.toByteArray());
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                zipOutputStream.close();
                return byteArrayOutputStream;
            } catch (IOException e) {
                e.printStackTrace();
                throw new IOException("创建zip文件失败");
            }
        } catch (Exception e) {
            logger.info("SFTP批量下载失败!异常：{}", e.getMessage());
            throw new RuntimeException("SFTP批量上传异常：", e);
        } finally {
            // 关闭连接
            disConnect();
        }
    }

    /**
     * 下载文件
     *
     * @param path     下载路径
     * @param fileName 文件名
     */
    public ByteArrayOutputStream downloadFile(String path, String fileName) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            if (!StringUtils.isEmpty(path)) {
                path = sftpConfig.getPath() + "/" + path;
                if (verifyDirectoryExists(path)) {
                    sftpUtil.channelSftp.cd(path);
                    sftpUtil.channelSftp.get(fileName, outputStream);
                }
            }
            logger.info("SFTP下载成功!文件名：{}", fileName);
        } catch (Exception e) {
            logger.info("SFTP下载失败!文件名：{}。异常：{}", fileName, e.getMessage());
            throw new RuntimeException("SFTP下载异常：", e);
        } finally {
            // 关闭连接
            disConnect();
        }
        return outputStream;
    }

    /**
     * 下载文件
     *
     * @param fileName 文件（全路径）
     */
    public ByteArrayOutputStream downloadFile(String fileName) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            sftpUtil.channelSftp.get(fileName, outputStream);
            logger.info("SFTP下载成功!文件名：{}", fileName);
        } catch (Exception e) {
            logger.info("SFTP下载失败!文件名：{}。异常：{}", fileName, e.getMessage());
            throw new RuntimeException("SFTP下载异常：", e);
        } finally {
            // 关闭连接
            disConnect();
        }
        return outputStream;
    }

    /**
     * 批量删除文件
     *
     * @param path 路径
     */
    public boolean batchRemoveFile(String path) {
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try {
            if (verifyDirectoryExists(path)) {
                Vector<ChannelSftp.LsEntry> files = sftpUtil.channelSftp.ls(path);
                if (files.size() > 0) {
                    sftpUtil.channelSftp.cd(path);
                    for (ChannelSftp.LsEntry file : files) {
                        String filename = file.getFilename();
                        if (!filename.startsWith(".")) {
                            sftpUtil.channelSftp.rm(filename);
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            logger.info("SFTP删除失败!异常：{}", e.getMessage());
            throw new RuntimeException("SFTP删除异常：", e);
        }
    }

    /**
     * 删除文件
     *
     * @param filePath 路径
     */
    public boolean removeFile(String filePath) {
        try {
            connect();
            SftpUtil sftpUtil = THREAD_LOCAL.get();
            sftpUtil.channelSftp.rm(filePath);
            logger.info("删除文件成功！文件：{}", filePath);
            return true;
        } catch (Exception e) {
            logger.info("SFTP删除失败!文件：{}。异常：{}", filePath, e.getMessage());
            throw new RuntimeException("SFTP删除异常：", e);
        } finally {
            disConnect();
        }
    }

    public boolean removeIfExistFile(String filePath) {
        try {
            connect();
            SftpUtil sftpUtil = THREAD_LOCAL.get();
            boolean isrm = true;
            try {
                SftpATTRS lstat = sftpUtil.channelSftp.lstat(filePath);
            } catch (Exception e) {
                isrm = false;
            }
            if (isrm) {
                sftpUtil.channelSftp.rm(filePath);
            }
            logger.info("删除文件成功！文件：{}", filePath);
            return true;
        } catch (Exception e) {
            logger.info("SFTP删除失败!文件：{}。异常：{}", filePath, e.getMessage());
            throw new RuntimeException("SFTP删除异常：", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 删除文件
     *
     * @param path 路径
     */
    public boolean removeDirectory(String path) {
        try {
            connect();
            SftpUtil sftpUtil = THREAD_LOCAL.get();
            sftpUtil.channelSftp.rmdir(path);
            logger.info("删除文件夹成功！文件夹：{}", path);
            return true;
        } catch (Exception e) {
            logger.info("SFTP删除文件夹失败{}!异常：{}", path, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("SFTP删除文件夹异常：", e);
        } finally {
            disConnect();
        }
    }

    /**
     * 删除文件
     *
     * @param path 路径
     */
    public boolean renameMoreFile(List<String> filepaths, List<String> filenames, String path) {
        try {
            connect();
            SftpUtil sftpUtil = THREAD_LOCAL.get();
            for (int i = 0; i < filepaths.size(); i++) {
                sftpUtil.channelSftp.rename(filepaths.get(i), sftpConfig.getPath() + "/" + path + "/" + filenames.get(i));
                logger.info("移动文件成功！文件：{}", filepaths.get(i));
            }
            return true;
        } catch (Exception e) {
            logger.info("SFTP移动失败!异常：{}", e.getMessage());
            throw new RuntimeException("SFTP移动异常：", e);
        }
    }

    /**
     * 验证目录是否存在
     *
     * @param path
     * @return
     */
    public boolean verifyDirectoryExists(String path) {
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        try {
            sftpUtil.channelSftp.cd(path);
            return true;
        } catch (Exception e) {
            logger.info(" {} 路径不存在", path);
            return false;
        }
    }

    /**
     * 文件流下载
     *
     * @param targetPath   目标服务器上的文件目录
     * @param sftpFileName 目标服务器文件名
     * @author ljx
     */
    public ByteArrayOutputStream downloadFileStream(String targetPath, String sftpFileName) throws Exception {
        // 登录服务器
        connect();
        SftpUtil sftpUtil = THREAD_LOCAL.get();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            sftpUtil.channelSftp.get(targetPath + "/" + sftpFileName, outputStream);
            return outputStream;
        } catch (SftpException e) {
            e.printStackTrace();
            return null;
        } finally {
            disConnect();
        }
    }

    /**
     * 拼接路径
     *
     * @param path1
     * @param path2
     * @return
     */
    public String splicingPath(String path1, String path2) {
        if (path2.startsWith("/")) {
            return path1 + path2;
        } else {
            return path1 + "/" + path2;
        }
    }
}