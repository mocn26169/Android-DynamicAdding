package com.dynamicadding.android_dynamicadding;

import java.io.File;

/**
 * Created by YXD002 on 2016/10/17.
 */

public class FileBean {

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 本地路径
     */
    private String localUrl;

    /**
     * 网络路径
     */
    private String WebUrl;

    /**
     * 大小
     */
    private String size;

    /**
     * 大小
     */
    private File file;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getWebUrl() {
        return WebUrl;
    }

    public void setWebUrl(String webUrl) {
        WebUrl = webUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "fileName='" + fileName + '\'' +
                ", localUrl='" + localUrl + '\'' +
                ", WebUrl='" + WebUrl + '\'' +
                ", size='" + size + '\'' +
                ", file=" + file +
                '}';
    }
}
