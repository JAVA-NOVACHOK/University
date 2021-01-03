package ua.com.nikiforov.mappers_dto;

import org.mapstruct.Mapper;
import ua.com.nikiforov.dto.RoomDTO;
import ua.com.nikiforov.models.Room;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapperDTO {

    public RoomDTO roomToRoomDTO(Room room);

    public Room roomDTOToRoom(RoomDTO roomDTO);

    public List<RoomDTO> roomsToRoomsDTO(List<Room> rooms);
}
