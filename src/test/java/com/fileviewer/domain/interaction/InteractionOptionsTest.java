package com.fileviewer.domain.interaction;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fileviewer.domain.model.DirectoryInfo;
import com.fileviewer.domain.model.FileInfo;

/**
 * Tests de {@link InteractionOptions} y {@link NodeInteractionPolicy}.
 */
class InteractionOptionsTest {

    @Test
    @DisplayName("none() no es activo")
    void none_isNotActive() {
        assertFalse(InteractionOptions.none().isActive());
    }

    @Test
    @DisplayName("of(policy) es activo")
    void of_customPolicy_isActive() {
        InteractionOptions opts = InteractionOptions.of(info -> {
        });
        assertTrue(opts.isActive());
    }

    @Test
    @DisplayName("getPolicy de none() devuelve NO_OP")
    void none_getPolicy_returnsNoOp() {
        assertSame(NodeInteractionPolicy.NO_OP, InteractionOptions.none().getPolicy());
    }

    @Test
    @DisplayName("NO_OP no lanza excepción con cualquier Info")
    void noOp_doesNotThrow() {
        assertDoesNotThrow(() -> NodeInteractionPolicy.NO_OP.onNodeClicked(
                new DirectoryInfo("root")));
        assertDoesNotThrow(() -> NodeInteractionPolicy.NO_OP.onNodeClicked(
                new FileInfo("x.txt", 0, "/x.txt", ".txt")));
    }

    @Test
    @DisplayName("La política personalizada se invoca al llamar onNodeClicked")
    void customPolicy_isInvokedOnClick() {
        boolean[] called = {false};
        NodeInteractionPolicy policy = info -> called[0] = true;
        InteractionOptions opts = InteractionOptions.of(policy);
        opts.getPolicy().onNodeClicked(new DirectoryInfo("dir"));
        assertTrue(called[0]);
    }
}
