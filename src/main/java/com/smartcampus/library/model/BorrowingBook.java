package com.smartcampus.library.model;

import java.util.List;

import com.smartcampus.common.ModelLocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowingBook {
	private List<Books> booksName;
	private ModelLocalDateTime borrowingDateTime;
	private String returnDay;
}
