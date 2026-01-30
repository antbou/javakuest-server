package ch.antbou.boundary.out;

import ch.antbou.boundary.dto.PlayerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartResponse {
    private String type;
    private List<PlayerDto> players;
}
