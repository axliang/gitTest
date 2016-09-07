package com.cmcc.hy.bigdata.weijifen.util;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmcc.hy.bigdata.weijifen.constants.ConfigConstants;

public class ConfigurationUtil {

	private static final Logger logger = LoggerFactory.getLogger(ConfigurationUtil.class);

	private static final String CLUSTER_CONFIG_FILE_NAME = "cluster.xml";

	public static Configuration loginAuthentication(String[] args, String specificFileName, Configuration conf) {
		if (args.length < 1) {
			logger.error("Config path doesn't exist!");
			System.exit(1);
		}

		String configPath = args[0] + File.separator;
		String clusterAbsolutePath = configPath + CLUSTER_CONFIG_FILE_NAME;
		String specificAbsolutePath = configPath + specificFileName;

		if (!(new File(clusterAbsolutePath)).exists()) {
			System.err.println(String.format("%s doesn't exist!", clusterAbsolutePath));
			System.exit(1);
		}

		if (!(new File(specificAbsolutePath)).exists()) {
			logger.error(String.format("%s doesn't exist!", specificAbsolutePath));
			System.exit(1);
		}
		// 添加集群配置
		conf.addResource(new Path(clusterAbsolutePath));
		// 设置配置文件所在目录
		conf.set(ConfigConstants.CONFIG_PATH, configPath);
		// 获取集群认证方式
		String loginWay = conf.get(ConfigConstants.LOGIN_WAY_KEY);
		// 根据部署地获取不同的部署配置
		Configuration result = choose(loginWay, configPath, conf);

		if (result == null) {
			logger.error("Configuration occured error!");
			System.exit(1);
		}
		// 最后添加特有的配置文件，防止属性被覆盖
		result.addResource(new Path(specificAbsolutePath));
		return result;
	}

	private static Configuration choose(String loginWay, String configPath, Configuration configuration) {
		Configuration conf = null;
		if ("shanghai".equals(loginWay)) {
			conf = ShanghaiLoginUtil.getConf(configuration);
			conf.addResource(new Path(configPath + "shanghai-hbase-site.xml"));
			conf.addResource(new Path(configPath + "shanghai-core-site.xml"));
			conf.addResource(new Path(configPath + "shanghai-mapred-site.xml"));
			conf.addResource(new Path(configPath + "shanghai-yarn-site.xml"));
		} else if ("shanghaiTest".equals(loginWay)) {
			conf = ShanghaiTestLoginUtil.getConf(configuration);
			conf.addResource(new Path(configPath + "shanghai-test-hbase-site.xml"));
			conf.addResource(new Path(configPath + "shanghai-test-core-site.xml"));
			conf.addResource(new Path(configPath + "shanghai-test-mapred-site.xml"));
			conf.addResource(new Path(configPath + "shanghai-test-yarn-site.xml"));
		} else if ("hubei".equals(loginWay)) {
			conf = configuration;
			conf.addResource(new Path(configPath + "hubei-hbase-site.xml"));
		} else if ("guizhou".equals(loginWay)) {
			conf = configuration;
			conf.addResource(new Path(configPath + "guizhou-hbase-site.xml"));
			conf.addResource(new Path(configPath + "guizhou-core-site.xml"));
			conf.addResource(new Path(configPath + "guizhou-mapred-site.xml"));
			conf.addResource(new Path(configPath + "guizhou-yarn-site.xml"));
			conf.addResource(new Path(configPath + "guizhou-hdfs-site.xml"));
			// Specific configuration
			conf.set("hadoop.security.bdoc.access.id", "bd82beb68b4cf7140278");
			conf.set("hadoop.security.bdoc.access.key", "3ae709619f1e02bb4e2a249fcffa8fa546f57c3d");
			conf.set("hadoop.security.proxy.user", "hy_bigdata");
			conf.set("mapreduce.job.bdoc.queuename", "credit");
		} else if (ConfigConstants.DEFAULT_LOGIN_WAY.equals(loginWay)) {
			conf = configuration;
		} else {
			logger.error("Lack of login way!");
		}
		return conf;
	}
}
