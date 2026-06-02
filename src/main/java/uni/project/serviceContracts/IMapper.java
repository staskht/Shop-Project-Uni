package uni.project.serviceContracts;

public interface IMapper<Model, DTO> {
    DTO toDto(Model model);
}
