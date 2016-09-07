package com.cmcc.hy.bigdata.weijifen.util;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.HdfsConfiguration;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemUtil {

	private static final Logger logger = LoggerFactory.getLogger(FileSystemUtil.class);

	private static DistributedFileSystem dfs = null;

	static {
		dfs = new DistributedFileSystem();
		HdfsConfiguration conf = new HdfsConfiguration();
		try {
			dfs.initialize(FileSystem.getDefaultUri(conf), conf);
		} catch (IOException e) {
			logger.error("FileSystemUtil initial error caused by DistributedFileSystem initial error.");
		}
	}

	/**
	 * Check path existence
	 * 
	 * @param path
	 * @return
	 */
	public static boolean exists(Path path) {
		if (path == null) {
			return false;
		}
		try {
			return dfs.exists(path);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * Get full path
	 * 
	 * @param is
	 * @return
	 */
	public static String getFullPath(InputSplit is) {
		if (is == null) {
			return null;
		}
		if (is instanceof FileSplit) {
			FileSplit fs = (FileSplit) is;
			return fs.getPath().toString();
		}
		try {
			// This case for TaggedInputSplit
			Method method = is.getClass().getMethod("getInputSplit", new Class<?>[] {});
			method.setAccessible(true);
			FileSplit fileSplit = (FileSplit) method.invoke(is);
			return fileSplit.getPath().toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
