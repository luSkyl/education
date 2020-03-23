package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lcy
 * @Date 2020/3/21
 * @Description
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
