package com.example.balanceGame.repository;

import com.example.balanceGame.entity.BoardHeartHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.example.balanceGame.entity.QBoardHeartHistory.boardHeartHistory;
@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardHeartHistoryRepository {
    private final EntityManager em;
    private final JPAQueryFactory qm;

    // 좋아요 한 유저 조회 메서드
    public BoardHeartHistory findHeartByBoardIdAndUserId(Long boardKey, Long userKey) {
        return qm.selectFrom(boardHeartHistory)
                .where(boardHeartHistory.board.boardKey.eq(boardKey).and(boardHeartHistory.userKey.eq(userKey)))
                .fetchOne();
    }

    // 좋아요 내역 저장
    public boolean save(BoardHeartHistory boardHeartHistory) {
        try {
            em.persist(boardHeartHistory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(BoardHeartHistory boardHeartHistory) {
        try {
            em.remove(boardHeartHistory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

