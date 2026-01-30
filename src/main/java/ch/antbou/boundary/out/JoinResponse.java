package ch.antbou.boundary.out;

import ch.antbou.boundary.dto.PlayerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinResponse {
    private String type;
    private PlayerDto player;
    private int playersRequired;
    private int connectedPlayers;
}
