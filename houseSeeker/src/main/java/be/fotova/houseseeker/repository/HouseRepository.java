package be.fotova.houseseeker.repository;

import be.fotova.houseseeker.entity.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<HouseEntity, Integer>
{
}
