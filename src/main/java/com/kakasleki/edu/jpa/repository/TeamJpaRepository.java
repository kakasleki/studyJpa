package com.kakasleki.edu.jpa.repository;

import com.kakasleki.edu.jpa.entity.Team;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TeamJpaRepository {
    @PersistenceContext
    private EntityManager em;

    public Team save(Team team) {
        this.em.persist(team);
        return team;
    }

    public void delete(Team team) {
        this.em.remove(team);
    }

    public List<Team> findAll() {
        return this.em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team team = this.em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public Long count() {
        return this.em.createQuery("select count(t) from Team t", Long.class)
                .getSingleResult();
    }
}
