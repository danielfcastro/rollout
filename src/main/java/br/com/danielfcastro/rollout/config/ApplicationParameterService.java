package br.com.danielfcastro.rollout.config;

public class ApplicationParameterService {

	public Object getProperty(String baseAddress, String nomeApp) {
		// TODO Auto-generated method stub
		if("base.address".equalsIgnoreCase(baseAddress)) return "127.0.0.1";
		if("base.port".equalsIgnoreCase(baseAddress)) return "80";
		else return null;
	}

}
