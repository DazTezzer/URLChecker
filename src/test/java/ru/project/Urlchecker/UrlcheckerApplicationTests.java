package ru.project.Urlchecker;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.project.Urlchecker.controller.UrlInfoController;
import ru.project.Urlchecker.dto.UrlInfoDTO;
import ru.project.Urlchecker.service.UrlInfoService;
import ru.project.Urlchecker.tables.UrlInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class UrlcheckerApplicationTests {

	private UrlInfoService urlInfoService = Mockito.mock(UrlInfoService.class);
	private UrlInfoController urlInfoController = new UrlInfoController(urlInfoService);

	@Test
	void testCreate() {
		UrlInfoDTO urlInfoDTO = new UrlInfoDTO();

		Mockito.when(urlInfoService.create(urlInfoDTO)).thenReturn(new UrlInfo());


		ResponseEntity<UrlInfo> responseEntity = urlInfoController.create(urlInfoDTO);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	void testReadAll() {

		Mockito.when(urlInfoService.readAll()).thenReturn(Collections.singletonList(new UrlInfo()));


		ResponseEntity<List<UrlInfo>> responseEntity = urlInfoController.readAll();

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(1, responseEntity.getBody().size());
	}

	@Test
	void testUpdate() {
		UrlInfo urlInfo = new UrlInfo();

		Mockito.when(urlInfoService.update(urlInfo)).thenReturn(new UrlInfo());


		ResponseEntity<UrlInfo> responseEntity = urlInfoController.update(urlInfo);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	}

	@Test
	void testDelete() {
		Long id = 1L;

		Mockito.doNothing().when(urlInfoService).delete(id);


		HttpStatus responseStatus = urlInfoController.delete(id);

		assertEquals(HttpStatus.OK, responseStatus);
	}

	@Test
	void testCheckIfUrl() {
		String url = "http://example.com";

		ResponseEntity<Map<String, Object>> expectedResponseEntity = ResponseEntity.ok(Collections.singletonMap("key", "value"));
		Mockito.when(urlInfoService.status(url)).thenReturn(expectedResponseEntity);


		ResponseEntity<Map<String, Object>> responseEntity = urlInfoController.checkIfUrl(url);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(expectedResponseEntity.getBody(), responseEntity.getBody());
	}

}
