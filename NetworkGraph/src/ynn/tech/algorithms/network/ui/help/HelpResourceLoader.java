package ynn.tech.algorithms.network.ui.help;

import java.net.URL;

import ynn.tech.algorithms.network.ui.util.ResourceLoader;

public class HelpResourceLoader implements ResourceLoader
{

	@Override
	public URL getResource(String resource)
	{
		return this.getClass().getResource(resource);
	}

}
