package com.echo.country.vo;

import com.echo.country.pojo.Contact;
import lombok.Data;

import java.util.List;

/**
 * @author Echo-9527
 * @Describe
 * @Version 1.0
 * @date 2022/3/8 14:59
 */
@Data
public class ContactVo {

	private String name;

	private List<Contact> contacts;


}
