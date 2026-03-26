package com.emiliosg23.domain.pipeline;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.emiliosg23.domain.model.DirectoryInfo;
import com.emiliosg23.domain.model.ExtensionInfo;
import com.emiliosg23.domain.model.FileInfo;
import com.emiliosg23.domain.model.Info;
import com.emiliosg23.tdas.trees.MultiTree;

/**
 * Pipeline de transformaciones del árbol de información.
 *
 * <p>
 * Mantiene un <em>registro</em> de transformaciones disponibles y un conjunto
 * de las que están actualmente activas. Al llamar a {@link #apply(MultiTree)},
 * copia el árbol original en profundidad y aplica las transformaciones activas
 * en el orden que respeta los prerequisitos declarados por cada
 * {@link TreeTransformation#requiredPredecessors()}.</p>
 *
 * <h3>Extensibilidad</h3>
 * <p>
 * Para añadir un nuevo modo basta con {@link #register(TreeTransformation)
 * registrar} la implementación. No hay que modificar ningún condicional
 * existente.</p>
 *
 * <h3>Reglas de dependencia</h3>
 * <ul>
 * <li>{@link #enable(String)} activa automáticamente los prerequisitos que aún
 * no estén activos.</li>
 * <li>{@link #disable(String)} desactiva en cascada todo lo que dependa de la
 * transformación desactivada.</li>
 * </ul>
 *
 * @see TreeTransformation
 */
public class TransformationPipeline {

    private final Map<String, TreeTransformation> registry = new LinkedHashMap<>();
    private final Set<String> active = new LinkedHashSet<>();

    /**
     * Registra una transformación disponible para uso futuro. No la activa.
     *
     * @param transformation transformación a registrar
     */
    public void register(TreeTransformation transformation) {
        registry.put(transformation.getId(), transformation);
    }

    /**
     * Activa una transformación. Activa en cascada los prerequisitos que no
     * estén ya activos.
     *
     * @param id ID de la transformación
     * @return {@code true} (el nuevo estado es activo)
     * @throws IllegalArgumentException si el ID no está registrado
     */
    public boolean enable(String id) {
        if (!registry.containsKey(id)) {
            throw new IllegalArgumentException("Unknown transformation: " + id);
        }
        for (String req : registry.get(id).requiredPredecessors()) {
            if (!active.contains(req)) {
                enable(req);
            }
        }
        active.add(id);
        return true;
    }

    /**
     * Desactiva una transformación. Desactiva en cascada todo lo que dependa de
     * ella.
     *
     * @param id ID de la transformación
     * @return {@code true} si estaba activa y fue desactivada
     */
    public boolean disable(String id) {
        if (!active.contains(id)) {
            return false;
        }
        for (Map.Entry<String, TreeTransformation> entry : registry.entrySet()) {
            if (active.contains(entry.getKey())
                    && entry.getValue().requiredPredecessors().contains(id)) {
                disable(entry.getKey());
            }
        }
        active.remove(id);
        return true;
    }

    /**
     * Alterna el estado de una transformación.
     *
     * @param id ID de la transformación
     * @return nuevo estado ({@code true} = activo)
     */
    public boolean toggle(String id) {
        if (active.contains(id)) {
            disable(id);
            return false;
        }
        enable(id);
        return true;
    }

    /**
     * @return {@code true} si la transformación indicada está activa
     */
    public boolean isActive(String id) {
        return active.contains(id);
    }

    /**
     * Desactiva todas las transformaciones activas.
     */
    public void reset() {
        active.clear();
    }

    /**
     * Copia en profundidad el árbol fuente y aplica las transformaciones
     * activas en el orden que respeta sus prerequisitos declarados.
     *
     * @param source árbol original (no se modifica)
     * @return árbol transformado, o {@code null} si {@code source} es null
     */
    public MultiTree<Info> apply(MultiTree<Info> source) {
        if (source == null) {
            return null;
        }
        MultiTree<Info> tree = deepCopy(source);
        for (String id : resolveOrder()) {
            TreeTransformation transform = registry.get(id);
            if (transform != null) {
                tree = transform.apply(tree);
            }
        }
        return tree;
    }

    // --- Resolución de orden topológico ---
    private List<String> resolveOrder() {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        for (String id : active) {
            visitOrder(id, visited, result);
        }
        return result;
    }

    private void visitOrder(String id, Set<String> visited, List<String> result) {
        if (visited.contains(id)) {
            return;
        }
        visited.add(id);
        TreeTransformation transform = registry.get(id);
        if (transform != null) {
            for (String req : transform.requiredPredecessors()) {
                if (active.contains(req)) {
                    visitOrder(req, visited, result);
                }
            }
        }
        result.add(id);
    }

    // --- Deep copy ---
    private static MultiTree<Info> deepCopy(MultiTree<Info> tree) {
        if (tree == null) {
            return null;
        }
        Info copiedContent = copyInfo(tree.getRoot().getContent());
        MultiTree<Info> copy = new MultiTree<>(copiedContent);
        for (MultiTree<Info> child : tree.getRoot().getChildren()) {
            copy.addChild(deepCopy(child));
        }
        return copy;
    }

    private static Info copyInfo(Info info) {
        if (info instanceof DirectoryInfo dir) {
            return new DirectoryInfo(dir.getName(), dir.getSize());
        }
        if (info instanceof FileInfo file) {
            return new FileInfo(file.getName(), file.getSize(), file.getFullPath(), file.getExtension());
        }
        if (info instanceof ExtensionInfo ext) {
            return new ExtensionInfo(ext.getName(), ext.getSize());
        }
        throw new IllegalArgumentException("Unknown Info type: " + info.getClass().getSimpleName());
    }
}
