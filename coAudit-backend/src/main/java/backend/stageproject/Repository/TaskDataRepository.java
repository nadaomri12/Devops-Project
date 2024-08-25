package backend.stageproject.Repository;

import backend.stageproject.Entity.Data;
import backend.stageproject.Entity.TaskData;
import backend.stageproject.Entity.TypeData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskDataRepository extends JpaRepository<TaskData, UUID> {

    Optional<TaskData> findByTaskIdAndDataId(UUID taskId, UUID dataId);

    Optional<TaskData> findByTaskIdAndDataIdAndType(UUID id, UUID id2, TypeData type);

    List<TaskData> findAllByTaskId(UUID id);

    TaskData findTopByCodeStartingWithOrderByCodeDesc(String baseCode);


    // Fetch all TaskData by taskId, and type

    @Query("SELECT td FROM TaskData td WHERE td.task.id = :taskId AND td.type = :type")
    List<TaskData> findAllByTaskOperationProcessIdAndType( UUID taskId, TypeData type);

}


