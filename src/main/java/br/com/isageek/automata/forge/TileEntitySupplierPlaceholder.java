package br.com.isageek.automata.forge;

import net.minecraft.tileentity.TileEntity;

import java.util.function.Supplier;
/***
 * Workaround circular dependency between registering
 * a TileEntityType Builder and the TileEntity Deferred register
 *
 * To register a TileEntity you need to build a TileEntityType
 * but, to build a TileEntityType you need the TileEntity coming from
 * the Registry. All of those are lazy, using Suppliers
 *
 * the solutions you find looking online usually
 * use static fields referencing each other,
 * making it really hard to figure out that exists a
 * circular dependency. Instead of using static fields
 * as globals, this SupplierPlaceholder makes explicit
 * what is going on.
 * @param <T>
 */
public class TileEntitySupplierPlaceholder implements Supplier<TileEntity> {

    private Supplier<? extends TileEntity> realSupplier;

    public void setRealSupplier(Supplier<? extends TileEntity> realSupplier){
        this.realSupplier = realSupplier;
    }

    @Override
    public TileEntity get() {
        return realSupplier.get();
    }
}
