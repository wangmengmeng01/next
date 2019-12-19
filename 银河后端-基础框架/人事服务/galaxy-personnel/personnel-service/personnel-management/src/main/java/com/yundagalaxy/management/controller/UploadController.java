package com.yundagalaxy.management.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UploadController
 *
 * @author Chill
 */
@RestController
@AllArgsConstructor
@RequestMapping("/notice/upload")
@Api(value = "对象存储接口", tags = "oss上传测试")
public class UploadController {
//
//	private MinioTemplate minioTemplate;
//
//	/**
//	 * minio上传demo
//	 *
//	 * @param file 上传文件
//	 * @return String
//	 */
//	@SneakyThrows
//	@PostMapping("put-minio-object")
//	public R<String> putMinioObject(@RequestParam MultipartFile file) {
//		minioTemplate.putFile(file);
//		String link = minioTemplate.fileLink(Objects.requireNonNull(file.getOriginalFilename()));
//		return R.data(link);
//	}

}
