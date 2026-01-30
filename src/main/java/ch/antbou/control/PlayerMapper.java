package ch.antbou.control;

import ch.antbou.boundary.dto.PlayerDto;
import ch.antbou.boundary.in.JoinRequest;
import ch.antbou.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PlayerMapper {
    @Mapping(target = "id", ignore = true)
    Player toEntity(JoinRequest request);

    PlayerDto toDto(Player player);
}
