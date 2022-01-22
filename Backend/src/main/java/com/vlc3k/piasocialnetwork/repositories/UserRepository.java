package com.vlc3k.piasocialnetwork.repositories;

import ch.qos.logback.core.status.Status;
import com.vlc3k.piasocialnetwork.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

}
