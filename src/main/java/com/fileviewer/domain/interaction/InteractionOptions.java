package com.fileviewer.domain.interaction;

/**
 * Opciones de interacción del treemap: encapsula la
 * {@link NodeInteractionPolicy} que se aplica cuando el usuario hace clic en un
 * nodo de archivo.
 *
 * <p>
 * Se obtiene desde {@code AppService} y se pasa al motor de renderizado antes
 * de cada render. Las instancias son inmutables; {@code AppService} las
 * intercambia enteras al activar o desactivar modos de interacción.</p>
 *
 * @see NodeInteractionPolicy
 */
public class InteractionOptions {

    private final NodeInteractionPolicy filePolicy;

    private InteractionOptions(NodeInteractionPolicy filePolicy) {
        this.filePolicy = filePolicy;
    }

    /**
     * Opciones sin interacción activa.
     */
    public static InteractionOptions none() {
        return new InteractionOptions(NodeInteractionPolicy.NO_OP);
    }

    /**
     * Opciones con la política proporcionada como interacción de archivos.
     *
     * @param policy política a aplicar al hacer clic
     */
    public static InteractionOptions of(NodeInteractionPolicy policy) {
        return new InteractionOptions(policy);
    }

    /**
     * @return política de interacción para nodos de archivo; nunca {@code null}
     */
    public NodeInteractionPolicy getPolicy() {
        return filePolicy;
    }

    /**
     * @return {@code true} si hay una política de interacción activa
     */
    public boolean isActive() {
        return filePolicy != NodeInteractionPolicy.NO_OP;
    }
}
