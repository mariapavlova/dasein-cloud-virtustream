package org.dasein.cloud.virtustream.compute;

import org.dasein.cloud.AbstractCapabilities;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.Requirement;
import org.dasein.cloud.compute.Platform;
import org.dasein.cloud.compute.VmState;
import org.dasein.cloud.compute.VolumeCapabilities;
import org.dasein.cloud.compute.VolumeFormat;
import org.dasein.cloud.util.Cache;
import org.dasein.cloud.util.CacheLevel;
import org.dasein.cloud.virtustream.Virtustream;
import org.dasein.util.uom.storage.Gigabyte;
import org.dasein.util.uom.storage.Storage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Describes the capabilities of Virtustream with respect to Dasein volume operations.
 * User: daniellemayne
 * Date: 06/03/2014
 * Time: 11:25
 */
public class VSVolumeCapabilities extends AbstractCapabilities<Virtustream> implements VolumeCapabilities {
    public VSVolumeCapabilities(@Nonnull Virtustream provider) {
        super(provider);
    }

    @Override
    public boolean canAttach(VmState vmState) throws InternalException, CloudException {
        return false;
    }

    @Override
    public boolean canDetach(VmState vmState) throws InternalException, CloudException {
        return false;
    }

    @Override
    public int getMaximumVolumeCount() throws InternalException, CloudException {
        return -2;
    }

    @Nullable
    @Override
    public Storage<Gigabyte> getMaximumVolumeSize() throws InternalException, CloudException {
        return new Storage<Gigabyte>(1024, Storage.GIGABYTE);
    }

    @Nonnull
    @Override
    public Storage<Gigabyte> getMinimumVolumeSize() throws InternalException, CloudException {
        return new Storage<Gigabyte>(1, Storage.GIGABYTE);
    }

    @Nonnull
    @Override
    public String getProviderTermForVolume(@Nonnull Locale locale) {
        return "Disk";
    }

    @Nonnull
    @Override
    public Requirement getVolumeProductRequirement() throws InternalException, CloudException {
        return Requirement.NONE;
    }

    @Override
    public boolean isVolumeSizeDeterminedByProduct() throws InternalException, CloudException {
        return false;
    }

    @Nonnull
    @Override
    public Iterable<String> listPossibleDeviceIds(@Nonnull Platform platform) throws InternalException, CloudException {
        Cache<String> cache;

        if( platform.isWindows() ) {
            cache = Cache.getInstance(getProvider(), "windowsDeviceIds", String.class, CacheLevel.CLOUD);
        }
        else {
            cache = Cache.getInstance(getProvider(), "unixDeviceIds", String.class, CacheLevel.CLOUD);
        }
        Iterable<String> ids = cache.get(getContext());

        if( ids == null ) {
            ArrayList<String> list = new ArrayList<String>();

            if( platform.isWindows() ) {
                list.add("hda");
                list.add("hdb");
                list.add("hdc");
                list.add("hdd");
                list.add("hde");
                list.add("hdf");
                list.add("hdg");
                list.add("hdh");
                list.add("hdi");
                list.add("hdj");
            }
            else {
                list.add("/dev/xvda");
                list.add("/dev/xvdb");
                list.add("/dev/xvdc");
                list.add("/dev/xvde");
                list.add("/dev/xvdf");
                list.add("/dev/xvdg");
                list.add("/dev/xvdh");
                list.add("/dev/xvdi");
                list.add("/dev/xvdj");
            }
            ids = Collections.unmodifiableList(list);
            cache.put(getContext(), ids);
        }
        return ids;
    }

    @Nonnull
    @Override
    public Iterable<VolumeFormat> listSupportedFormats() throws InternalException, CloudException {
        return Collections.singletonList(VolumeFormat.BLOCK);
    }

    @Nonnull
    @Override
    public Requirement requiresVMOnCreate() throws InternalException, CloudException {
        return Requirement.REQUIRED;
    }
}