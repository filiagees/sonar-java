/*
 * SonarQube Java
 * Copyright (C) 2012-2021 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.eclipse.jdt.core.dom.ASTUtils;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.sonar.plugins.java.api.semantic.Symbol;
import org.sonar.plugins.java.api.semantic.Type;
import org.sonar.plugins.java.api.tree.MethodTree;

final class JMethodSymbol extends JSymbol implements Symbol.MethodSymbol {

  /**
   * Cache for {@link #parameterTypes()}.
   */
  private List<Type> parameterTypes;

  /**
   * Cache for {@link #returnType()}.
   */
  private TypeSymbol returnType;

  /**
   * Cache for {@link #thrownTypes()}.
   */
  private List<Type> thrownTypes;

  /**
   * Cache for {@link #overriddenSymbol()}.
   */
  private List<MethodSymbol> overriddenSymbols;
  private MethodSymbol firstOverridenSymbol;

  private final String signature;

  JMethodSymbol(JSema sema, IMethodBinding methodBinding) {
    super(sema, methodBinding);
    this.signature = methodBinding().getDeclaringClass().getBinaryName()
      + "#" + name()
      + ASTUtils.signature(methodBinding().getMethodDeclaration());
  }

  IMethodBinding methodBinding() {
    return (IMethodBinding) binding;
  }

  @Override
  public List<Type> parameterTypes() {
    if (parameterTypes == null) {
      parameterTypes = sema.types(methodBinding().getParameterTypes());
    }
    return parameterTypes;
  }

  /**
   * @since 6.0 returns void type for constructors instead of {@code null}
   */
  @Override
  public TypeSymbol returnType() {
    if (returnType == null) {
      returnType = sema.typeSymbol(methodBinding().getReturnType());
    }
    return returnType;
  }

  @Override
  public List<Type> thrownTypes() {
    if (thrownTypes == null) {
      thrownTypes = sema.types(methodBinding().getExceptionTypes());
    }
    return thrownTypes;
  }

  @Nullable
  @Override
  public MethodSymbol overriddenSymbol() {
    if (overriddenSymbols == null) {
      // only compute overridenSymbols once
      overriddenSymbols();
    }
    return firstOverridenSymbol;
  }

  @Override
  public List<MethodSymbol> overriddenSymbols() {
    if (overriddenSymbols == null) {
      overriddenSymbols = convertOverriddenSymbols();
      if (!overriddenSymbols.isEmpty()) {
        firstOverridenSymbol = overriddenSymbols.get(0);
      }
    }
    return overriddenSymbols;
  }

  private List<MethodSymbol> convertOverriddenSymbols() {
    IMethodBinding methodBinding = methodBinding();
    ITypeBinding declaringClass = methodBinding.getDeclaringClass();
    return findOverridesInParentTypes(methodBinding::overrides, declaringClass).stream()
      .map(sema::methodSymbol)
      .collect(Collectors.toList());
  }

  private Set<IMethodBinding> findOverridesInParentTypes(Predicate<IMethodBinding> overridesCondition, ITypeBinding type) {
    Set<IMethodBinding> bindings = new LinkedHashSet<>();
    if (type.isInterface()) {
      // check Object for interfaces forcing overrides from Object
      bindings.addAll(findOverridesInTypes(overridesCondition, sema.resolveType("java.lang.Object")));
    } else if (!"java.lang.Object".equals(type.getQualifiedName())) {
      bindings.addAll(findOverridesInTypes(overridesCondition, type.getSuperclass()));
    }
    bindings.addAll(findOverridesInTypes(overridesCondition, type.getInterfaces()));
    return bindings;
  }

  private Set<IMethodBinding> findOverridesInTypes(Predicate<IMethodBinding> overridesCondition, ITypeBinding... types) {
    Set<IMethodBinding> overrides = new LinkedHashSet<>();

    for (ITypeBinding type : types) {
      // check current type
      Stream.of(type.getDeclaredMethods())
        .filter(overridesCondition)
        .findFirst()
        .ifPresent(overrides::add);
      // check other inheritance levels
      overrides.addAll(findOverridesInParentTypes(overridesCondition, type));
    }
    return overrides;
  }

  @Override
  public String signature() {
    return signature;
  }

  @Nullable
  @Override
  public MethodTree declaration() {
    return (MethodTree) super.declaration();
  }

}
