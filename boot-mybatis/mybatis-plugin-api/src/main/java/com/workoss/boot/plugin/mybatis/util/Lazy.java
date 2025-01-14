package com.workoss.boot.plugin.mybatis.util;

import com.workoss.boot.annotation.lang.Nullable;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 懒加载
 *
 * @param <T> 泛型
 * @author workoss
 */
public class Lazy<T> implements Supplier<T> {

	private static final Lazy<?> EMPTY = new Lazy<>(() -> null, null, true);

	private final Supplier<? extends T> supplier;

	private @Nullable T value;

	private volatile boolean resolved;

	private Lazy(Supplier<? extends T> supplier) {
		this(supplier, null, false);
	}

	/**
	 * Creates a new {@link Lazy} for the given {@link Supplier}, value and whether it has
	 * been resolved or not.
	 * @param supplier must not be {@literal null}.
	 * @param value can be {@literal null}.
	 * @param resolved whether the value handed into the constructor represents a resolved
	 * value.
	 */
	private Lazy(Supplier<? extends T> supplier, @Nullable T value, boolean resolved) {

		this.supplier = supplier;
		this.value = value;
		this.resolved = resolved;
	}

	/**
	 * Creates a new {@link Lazy} to produce an object lazily.
	 * @param <T> the type of which to produce an object of eventually.
	 * @param supplier the {@link Supplier} to create the object lazily.
	 * @return
	 */
	public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
		return new Lazy<>(supplier);
	}

	/**
	 * Creates a new {@link Lazy} to return the given value.
	 * @param <T> the type of the value to return eventually.
	 * @param value the value to return.
	 * @return lazy
	 */
	public static <T> Lazy<T> of(T value) {

		ObjectUtil.notNull(value, "Value must not be null!");

		return new Lazy<>(() -> value);
	}

	/**
	 * Creates a pre-resolved empty {@link Lazy}.
	 * @return lazy
	 */
	@SuppressWarnings("unchecked")
	public static <T> Lazy<T> empty() {
		return (Lazy<T>) EMPTY;
	}

	/**
	 * Returns the value created by the configured {@link Supplier}. Will return the
	 * calculated instance for subsequent lookups.
	 * @return lazy
	 */
	@Override
	public T get() {

		T value = getNullable();

		if (value == null) {
			throw new IllegalStateException("Expected lazy evaluation to yield a non-null value but got null!");
		}

		return value;
	}

	/**
	 * Returns the {@link Optional} value created by the configured {@link Supplier},
	 * allowing the absence of values in contrast to {@link #get()}. Will return the
	 * calculated instance for subsequent lookups.
	 * @return lazy
	 */
	public Optional<T> getOptional() {
		return Optional.ofNullable(getNullable());
	}

	/**
	 * Returns a new Lazy that will consume the given supplier in case the current one
	 * does not yield in a result.
	 * @param supplier must not be {@literal null}.
	 * @return lazy
	 */
	public Lazy<T> or(Supplier<? extends T> supplier) {

		ObjectUtil.notNull(supplier, "Supplier must not be null!");

		return Lazy.of(() -> orElseGet(supplier));
	}

	/**
	 * Returns a new Lazy that will return the given value in case the current one does
	 * not yield in a result.
	 * @return lazy
	 */
	public Lazy<T> or(T value) {

		ObjectUtil.notNull(value, "Value must not be null!");

		return Lazy.of(() -> orElse(value));
	}

	/**
	 * Returns the value of the lazy computation or the given default value in case the
	 * computation yields {@literal null}.
	 * @param value
	 * @return lazy
	 */
	@Nullable
	public T orElse(@Nullable T value) {

		T nullable = getNullable();

		return nullable == null ? value : nullable;
	}

	/**
	 * Returns the value of the lazy computation or the value produced by the given
	 * {@link Supplier} in case the original value is {@literal null}.
	 * @param supplier must not be {@literal null}.
	 * @return lazy
	 */
	@Nullable
	private T orElseGet(Supplier<? extends T> supplier) {

		ObjectUtil.notNull(supplier, "Default value supplier must not be null!");

		T value = getNullable();

		return value == null ? supplier.get() : value;
	}

	/**
	 * Creates a new {@link Lazy} with the given {@link Function} lazily applied to the
	 * current one.
	 * @param function must not be {@literal null}.
	 * @return lazy
	 */
	public <S> Lazy<S> map(Function<? super T, ? extends S> function) {

		ObjectUtil.notNull(function, "Function must not be null!");

		return Lazy.of(() -> function.apply(get()));
	}

	/**
	 * Creates a new {@link Lazy} with the given {@link Function} lazily applied to the
	 * current one.
	 * @param function must not be {@literal null}.
	 * @return lazy
	 */
	public <S> Lazy<S> flatMap(Function<? super T, Lazy<? extends S>> function) {

		ObjectUtil.notNull(function, "Function must not be null!");

		return Lazy.of(() -> function.apply(get()).get());
	}

	/**
	 * Returns the value of the lazy evaluation.
	 * @return t
	 */
	@Nullable
	public T getNullable() {

		if (resolved) {
			return value;
		}

		this.value = supplier.get();
		this.resolved = true;

		return value;
	}

	@Override
	public boolean equals(@Nullable Object o) {

		if (this == o) {
			return true;
		}

		if (!(o instanceof Lazy)) {
			return false;
		}

		Lazy<?> lazy = (Lazy<?>) o;

		if (resolved != lazy.resolved) {
			return false;
		}

		if (!ObjectUtil.nullSafeEquals(supplier, lazy.supplier)) {
			return false;
		}

		return ObjectUtil.nullSafeEquals(value, lazy.value);
	}

	@Override
	public int hashCode() {

		int result = ObjectUtil.nullSafeHashCode(supplier);

		result = 31 * result + ObjectUtil.nullSafeHashCode(value);
		result = 31 * result + (resolved ? 1 : 0);

		return result;
	}

}
