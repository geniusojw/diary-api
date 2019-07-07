package org.jerrioh.diary.domain.repo;

import org.jerrioh.diary.domain.App;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<App, String> {
	
	public App findTopByOrderByVersionDesc();
}
