package org.zenfile.service.storage.base;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.zenfile.model.storage.param.ProxyTransferParam;
import org.zenfile.service.config.SystemConfigService;
import org.zenfile.service.storage.StorageSourceService;
import org.zenfile.utils.StringUtils;

import java.io.InputStream;

/**
 * 代理传输类
 */
public abstract class AbstractProxyTransferService<P extends ProxyTransferParam> extends AbstractBaseFileService<P> {

    @javax.annotation.Resource
    private SystemConfigService systemConfigService;

    @javax.annotation.Resource
    StorageSourceService storageSourceService;

    /**
     * 代理下载URL前缀
     */
    public static final String PROXY_DOWNLOAD_LINK_PREFIX = "/pd";

    /**
     * 代理上传URL前缀
     */

    public static final String PROXY_UPLOAD_LINK_PREFIX = "/file/upload";

    /**
     * 获取系统的下载URL
     * @param pathAndName 文件路径+文件名
     * @return 下载URL
     */
    @Override
    public String getDownloadUrl(String pathAndName) {
        String signature = "";

        if(getParam().isPrivate()){
           // 生成签名
        }

        // 获取域名
        String domain = systemConfigService.getDomain();
        String storageKey = storageSourceService.getStorageKeyById(getStorageId());

        return StringUtils.concat(domain, PROXY_DOWNLOAD_LINK_PREFIX, storageKey, pathAndName) + signature;
    }

    /**
     * 获取系统的上传URL
     * @param path 上传的路径
     * @param name 上传文件名称
     * @param size 上传文件大小
     * @return  上传URL
     */
    @Override
    public String getUploadUrl(String path, String name, Long size) {
        // TODO 是否要对上传的大小做限制
        String domain = systemConfigService.getDomain();
        String storageKey = storageSourceService.getStorageKeyById(getStorageId());
        String pathAndName = StringUtils.concat(path, name);
        return StringUtils.concat(domain, PROXY_UPLOAD_LINK_PREFIX, storageKey, pathAndName);
    }

    /**
     * 上传文件
     * @param pathAndName 上传的路径
     * @param inputStream 文件流
     */
    public abstract void uploadFile(String pathAndName, InputStream inputStream);

    /**
     * 文件下载
     * @param pathAndName 下载路径
     * @return 下载流
     */
    public abstract ResponseEntity<Resource> downloadToStream(String pathAndName);
}
