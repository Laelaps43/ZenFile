package org.zenfile.utils;

import cn.hutool.system.oshi.OshiUtil;
import org.zenfile.model.utils.FileStoreInfo;
import oshi.software.os.OSFileStore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 该类用来获取部署设备的信息
 */
public class MachineInfoUtils {

    /**
     * 获取机器的所有分区的信息
     */
    public static List<FileStoreInfo> getMachineStorageInfo(){
        List<OSFileStore> fileStores = OshiUtil.getOs().getFileSystem().getFileStores();
        return fileStores.parallelStream().map(osFileStore -> new FileStoreInfo(osFileStore.getName(),
                osFileStore.getVolume(), osFileStore.getTotalSpace(), osFileStore.getFreeSpace())).collect(Collectors.toList());
    }
}
