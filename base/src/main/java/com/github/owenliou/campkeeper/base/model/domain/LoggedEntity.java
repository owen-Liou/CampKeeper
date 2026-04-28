package com.github.owenliou.campkeeper.base.model.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

public interface LoggedEntity extends Serializable {

	LocalDateTime getCreatedDate();

	void setCreatedDate(LocalDateTime createdDate);

	LocalDateTime getUpdatedDate();

	void setUpdatedDate(LocalDateTime updatedDate);

}
