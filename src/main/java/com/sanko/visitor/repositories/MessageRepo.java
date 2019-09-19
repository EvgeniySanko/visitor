package com.sanko.visitor.repositories;

import com.sanko.visitor.entities.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Long> {
}
