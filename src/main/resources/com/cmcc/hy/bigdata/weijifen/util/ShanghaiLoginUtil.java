package com.cmcc.hy.bigdata.weijifen.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.AppConfigurationEntry.LoginModuleControlFlag;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.security.authentication.util.KerberosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShanghaiLoginUtil {

	private static final Logger LOG = LoggerFactory
			.getLogger(ShanghaiTestLoginUtil.class);

	public static Configuration getConf(Configuration conf) {

		LOG.info("jinru Configuration");
		// 设置zookeeper server principal
		System.setProperty("zookeeper.server.principal",
				"zookeeper/hadoop.hadoop.com");
		// 设置keberos认证方式，注[1]

		conf.set("hbase.security.authentication", "kerberos");
		System.setProperty("java.security.krb5.conf", "/opt/client/keytab/krb5.conf");
		conf.set("username.client.keytab.file", "/opt/client/keytab/user.keytab");
		conf.set("username.client.kerberos.principal", "disney_B@HADOOP_B.COM");
		setJaasConf("Client", "disney_B@HADOOP_B.COM", "/opt/client/keytab/user.keytab");
		try {
			User.login(conf, "username.client.keytab.file",
					"username.client.kerberos.principal", InetAddress
							.getLocalHost().getCanonicalHostName());
		} catch (UnknownHostException e) {
			LOG.error(e.getMessage(), e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
		LOG.info("hbase.zookeeper.property.clientPort:"
				+ conf.get("hbase.zookeeper.property.clientPort"));
		LOG.info("hbase.zookeeper.quorum: "
				+ conf.get("hbase.zookeeper.quorum"));
		LOG.info("zookeeper.server.principal:"
				+ System.getProperty("zookeeper.server.principal"));
		LOG.info("java.security.auth.login.config:"
				+ System.getProperty("java.security.auth.login.config"));
		LOG.info("zookeeper.sasl.clientconfig:"
				+ System.getProperty("zookeeper.sasl.clientconfig"));
		LOG.info("javax.security.auth.login.Configuration:"
				+ javax.security.auth.login.Configuration.getConfiguration());
		LOG.info("javax.security.auth.login.Configuration Client Entry:"
				+ javax.security.auth.login.Configuration.getConfiguration()
						.getAppConfigurationEntry("Client"));
		LOG.info("chulai Configuration");
		return conf;
	}

	public static void setJaasConf(String loginContextName, String principal,
			String keytabFile) {
		JaasConfiguration conf = new JaasConfiguration(loginContextName,
				principal, keytabFile);
		javax.security.auth.login.Configuration.setConfiguration(conf);
	}

	/**
	 * copy from hbase zkutil 0.94&0.98 A JAAS configuration that defines the
	 * login modules that we want to use for login.
	 */
	private static class JaasConfiguration extends
			javax.security.auth.login.Configuration {
		// private static final String SERVER_KEYTAB_KERBEROS_CONFIG_NAME =
		// "zookeeper-server-keytab-kerberos";
		// private static final String CLIENT_KEYTAB_KERBEROS_CONFIG_NAME =
		// "zookeeper-client-keytab-kerberos";

		private static final Map<String, String> BASIC_JAAS_OPTIONS = new HashMap<String, String>();
		static {
			String jaasEnvVar = System.getenv("HBASE_JAAS_DEBUG");
			if (jaasEnvVar != null && "true".equalsIgnoreCase(jaasEnvVar)) {
				BASIC_JAAS_OPTIONS.put("debug", "true");
			}
		}

		private static final Map<String, String> KEYTAB_KERBEROS_OPTIONS = new HashMap<String, String>();
		static {
			KEYTAB_KERBEROS_OPTIONS.put("doNotPrompt", "true");
			KEYTAB_KERBEROS_OPTIONS.put("storeKey", "true");
			KEYTAB_KERBEROS_OPTIONS.put("refreshKrb5Config", "true");
			KEYTAB_KERBEROS_OPTIONS.putAll(BASIC_JAAS_OPTIONS);
		}

		private static final AppConfigurationEntry KEYTAB_KERBEROS_LOGIN = new AppConfigurationEntry(
				KerberosUtil.getKrb5LoginModuleName(),
				LoginModuleControlFlag.REQUIRED, KEYTAB_KERBEROS_OPTIONS);

		private static final AppConfigurationEntry[] KEYTAB_KERBEROS_CONF = new AppConfigurationEntry[] { KEYTAB_KERBEROS_LOGIN };

		private javax.security.auth.login.Configuration baseConfig;
		private final String loginContextName;
		private final boolean useTicketCache;
		private final String keytabFile;
		private final String principal;

		@SuppressWarnings("unused")
		public JaasConfiguration(String loginContextName, String principal) {
			this(loginContextName, principal, null, true);
		}

		public JaasConfiguration(String loginContextName, String principal,
				String keytabFile) {
			this(loginContextName, principal, keytabFile, keytabFile == null
					|| keytabFile.length() == 0);
		}

		private JaasConfiguration(String loginContextName, String principal,
				String keytabFile, boolean useTicketCache) {
			try {
				this.baseConfig = javax.security.auth.login.Configuration
						.getConfiguration();
			} catch (SecurityException e) {
				this.baseConfig = null;
			}
			this.loginContextName = loginContextName;
			this.useTicketCache = useTicketCache;
			this.keytabFile = keytabFile;
			this.principal = principal;
			LOG.info("JaasConfiguration loginContextName=" + loginContextName
					+ " principal=" + principal + " useTicketCache="
					+ useTicketCache + " keytabFile=" + keytabFile);
		}

		public AppConfigurationEntry[] getAppConfigurationEntry(String appName) {
			if (loginContextName.equals(appName)) {
				if (!useTicketCache) {
					KEYTAB_KERBEROS_OPTIONS.put("keyTab", keytabFile);
					KEYTAB_KERBEROS_OPTIONS.put("useKeyTab", "true");
				}
				KEYTAB_KERBEROS_OPTIONS.put("principal", principal);
				KEYTAB_KERBEROS_OPTIONS.put("useTicketCache",
						useTicketCache ? "true" : "false");
				return KEYTAB_KERBEROS_CONF;
			}
			if (baseConfig != null)
				return baseConfig.getAppConfigurationEntry(appName);
			return (null);
		}
	}
}
