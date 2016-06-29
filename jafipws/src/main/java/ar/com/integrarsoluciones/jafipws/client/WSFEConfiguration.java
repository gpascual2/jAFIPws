/**
 * 
 */
package ar.com.integrarsoluciones.jafipws.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * @author pascualg
 *
 */
@Configuration
@ImportResource("classpath:ApplicationContext.xml")
public class WSFEConfiguration {
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("wsfe.wsdl");
		return marshaller;
	}

	@Bean
	public WSFEClient wsfeClient(Jaxb2Marshaller marshaller){ //, String defaultUri) {
		WSFEClient client = new WSFEClient();
		//client.setDefaultUri(defaultUri);
		client.setDefaultUri("https://wswhomo.afip.gov.ar/wsfev1/service.asmx");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}

}
