package com.emiliosg23.domain.interaction;

import com.emiliosg23.domain.model.Info;

/**
 * Política de interacción que define qué ocurre cuando el usuario hace clic en
 * un nodo del treemap.
 *
 * <p>
 * El modo ejecutable usa
 * {@link com.emiliosg23.infrastructure.interaction.OpenFileInteractionPolicy}.
 * Para desactivar la interacción se usa la constante {@link #NO_OP}.</p>
 *
 * <p>
 * Para añadir un nuevo comportamiento de interacción basta con implementar esta
 * interfaz e inyectarla en {@link InteractionOptions}.</p>
 *
 * @see InteractionOptions
 */
@FunctionalInterface
public interface NodeInteractionPolicy {

    /**
     * Ejecuta la acción asociada al clic sobre el nodo.
     *
     * @param info información del nodo sobre el que se hizo clic
     */
    void onNodeClicked(Info info);

    /**
     * Política nula: no realiza ninguna acción.
     */
    NodeInteractionPolicy NO_OP = info -> {
    };
}
