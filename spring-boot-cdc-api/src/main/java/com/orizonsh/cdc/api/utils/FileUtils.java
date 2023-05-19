package com.orizonsh.cdc.api.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FileUtils {

	/** Log API */
	private static final Logger log = LogManager.getLogger(FileUtils.class);

	/**
	 * ファイルを作成する。
	 *
	 * @param pathName ファイルパス
	 * @throws IOException
	 */
	public static void createFile(String pathName) throws IOException {

		log.debug("pathName：{}", pathName);
		File file = new File(pathName);

		if (!file.getParentFile().exists()) {
			Files.createDirectories(Path.of(file.getParent()));
		}

		if (!file.exists()) {
			Files.createFile(Path.of(file.getAbsolutePath()));
		}
	}
}
