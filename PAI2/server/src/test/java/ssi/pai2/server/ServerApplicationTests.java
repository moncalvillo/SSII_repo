package ssi.pai2.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.web.servlet.MockMvc;

import ssi.pai2.server.service.ServerService;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)

class ServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ServerService serverService;

	Map<String, String> createParams() throws NoSuchAlgorithmException {
		String origen = "12345678";
		String destino = "87654321";
		String cantidad = "100";
		String nonce = UUID.randomUUID().toString();
		String mac = serverService.createMAC(origen + destino + cantidad + nonce, "challenge");

		Map<String, String> params = new HashMap<>();
		params.put("origen", origen);
		params.put("destino", destino);
		params.put("cantidad", cantidad);
		params.put("nonce", nonce);
		params.put("mac", mac);

		return params;
	}

	@Test
	void correctVerification() throws Exception {
		var params = createParams();
		String json = new ObjectMapper().writeValueAsString(params);
		this.mockMvc.perform(post("/server/proxy").content(json).contentType("application/json")).andDo(print())
				.andExpect(status().isOk());

	}

	@Test
	void modifyDestinyAttack() throws Exception {
		var params = createParams();
		params.put("attackType", "modifyDestiny");

		String json = new ObjectMapper().writeValueAsString(params);
		this.mockMvc.perform(post("/server/proxy").content(json).contentType("application/json")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("{'mensaje': 'INTEGRATION_ERROR'}"));

	}

	@Test
	void modifyQuantityAttack() throws Exception {
		var params = createParams();
		params.put("attackType", "modifyQuantity");

		String json = new ObjectMapper().writeValueAsString(params);
		this.mockMvc.perform(post("/server/proxy").content(json).contentType("application/json")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("{'mensaje': 'INTEGRATION_ERROR'}"));

	}

	@Test
	void modifyNonceAttack() throws Exception {
		var params = createParams();
		params.put("attackType", "modifyNonce");

		String json = new ObjectMapper().writeValueAsString(params);
		this.mockMvc.perform(post("/server/proxy").content(json).contentType("application/json")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("{'mensaje': 'INTEGRATION_ERROR'}"));

	}

	@Test
	void replyAttack() throws Exception {
		var params = createParams();
		params.put("attackType", "replyAttack");

		String json = new ObjectMapper().writeValueAsString(params);
		this.mockMvc.perform(post("/server/proxy").content(json).contentType("application/json")).andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json("{'mensaje': 'INTEGRATION_ERROR'}"));

	}

}
