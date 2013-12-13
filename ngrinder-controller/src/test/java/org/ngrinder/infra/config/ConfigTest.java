/* 
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.ngrinder.infra.config;

import org.junit.Before;
import org.junit.Test;
import org.ngrinder.common.constant.Constants;
import org.ngrinder.common.model.Home;
import org.ngrinder.common.util.PropertiesWrapper;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.util.Properties;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConfigTest implements Constants {

	private MockConfig config;

	@Before
	public void before() {
		System.setProperty("unit-test", "");
		config = new MockConfig();
		config.init();
	}

	@Test
	public void testDefaultHome() {
		Home home = config.getHome();
		File oracle = new File(System.getProperty("user.home"), ".ngrinder");
		assertThat(home.getDirectory(), is(oracle));
		assertThat(home.getPluginsDirectory(), is(new File(oracle, "plugins")));
	}

	@Test
	public void testGetMonitorPort() {
		int port = config.getMonitorPort();
		assertThat(port, not(0));
	}

	@Test
	public void testTestMode() {
		PropertiesWrapper wrapper = mock(PropertiesWrapper.class);
		config.setSystemProperties(wrapper);
		// When testmode false and pluginsupport is true, it should be true
		when(wrapper.getPropertyBoolean("testmode", false)).thenReturn(false);
		when(wrapper.getPropertyBoolean("pluginsupport", true)).thenReturn(false);
		assertThat(config.isPluginSupported(), is(false));

		// When testmode true and pluginsupport is false, it should be false
		when(wrapper.getPropertyBoolean("testmode", false)).thenReturn(true);
		when(wrapper.getPropertyBoolean("pluginsupport", true)).thenReturn(false);
		assertThat(config.isPluginSupported(), is(false));

		// When testmode false and pluginsupport is false, it should be false
		when(wrapper.getPropertyBoolean("testmode", false)).thenReturn(false);
		when(wrapper.getPropertyBoolean("pluginsupport", true)).thenReturn(false);
		assertThat(config.isPluginSupported(), is(false));

		// When testmode true and pluginsupport is true, it should be false
		when(wrapper.getPropertyBoolean("testmode", false)).thenReturn(true);
		when(wrapper.getPropertyBoolean("pluginsupport", true)).thenReturn(true);
		assertThat(config.isPluginSupported(), is(false));

		when(wrapper.getPropertyBoolean("testmode", false)).thenReturn(true);
		when(wrapper.getPropertyBoolean("security", false)).thenReturn(true);
		assertThat(config.isSecurityEnabled(), is(false));

		when(wrapper.getPropertyBoolean("testmode", false)).thenReturn(false);
		when(wrapper.getPropertyBoolean("security", false)).thenReturn(true);
		assertThat(config.isSecurityEnabled(), is(true));

		when(wrapper.getPropertyBoolean("testmode", false)).thenReturn(false);
		when(wrapper.getPropertyBoolean("security", false)).thenReturn(false);
		assertThat(config.isSecurityEnabled(), is(false));
	}

	@Test
	public void testPolicyFileLoad() {
		String processAndThreadPolicyScript = config.getProcessAndThreadPolicyScript();
		assertThat(processAndThreadPolicyScript, containsString("function"));
	}

	@Test
	public void testVersionString() {
		String version = config.getVersion();
		assertThat(version, not("UNKNOWN"));
	}


	@Test
	public void testLoadExtendProperties() {
		config.cluster = true;
		Properties wrapper = new Properties();
		wrapper.put(NGRINDER_PROP_REGION, "TestNewRegion");
		config.doRealOnRegion = true;
		// set mock exHome and test
		Home mockExHome = mock(Home.class);
		when(mockExHome.getProperties("system-ex.conf")).thenReturn(wrapper);
		when(mockExHome.exists()).thenReturn(true);
		ReflectionTestUtils.setField(config, "exHome", mockExHome);
		config.loadSystemProperties();
		assertThat(config.getRegion(), is("TestNewRegion"));
	}
}
