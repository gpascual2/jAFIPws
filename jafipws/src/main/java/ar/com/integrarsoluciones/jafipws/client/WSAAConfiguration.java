package ar.com.integrarsoluciones.jafipws.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class WSAAConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("wsaa.wsdl");
		return marshaller;
	}

	@Bean
	public WSAAClient wsaaClient(Jaxb2Marshaller marshaller) {
		WSAAClient client = new WSAAClient();
		client.setDefaultUri("https://wsaahomo.afip.gov.ar/ws/services/LoginCms");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}

