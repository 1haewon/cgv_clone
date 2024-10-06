package DTO;

import lombok.Data;

@Data
public class TheaterRequestDTO {
    private Long id;  // 극장 ID
    private String name;  // 극장 이름
    private Long movieId;  // 영화 ID (프론트엔드에서 받아올 필드)
}