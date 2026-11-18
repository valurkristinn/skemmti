package is.hi.skemmti.service;

import is.hi.skemmti.ContextBase;

import org.junit.jupiter.api.BeforeEach;

abstract class BaseServiceTests extends ContextBase {

    protected Long adminId;
    protected Long organizerId;
    protected Long memberId;

    @BeforeEach
    void loadIds() {
        adminId = idOf(ADMIN);
        organizerId = idOf(ORGANIZER);
        memberId = idOf(MEMBER);
    }
}
