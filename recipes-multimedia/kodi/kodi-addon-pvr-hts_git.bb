SUMMARY = "Kodi Media Center PVR plugins"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://src/client.cpp;md5=c8f6b73c5bc1048a3d6506700a7a91d2"

DEPENDS = " \
            zip-native \
            p8platform \
            kodi-platform \
          "

SRCREV_pvrhts = "7f75b70527922aef953123ff97ebaa22d9fb7cb4"

SRCREV_FORMAT = "pvrhts"

PV = "16.0+gitr${SRCPV}"
SRC_URI = "git://github.com/kodi-pvr/pvr.hts.git;branch=Jarvis;destsuffix=pvr.hts;name=pvrhts \
           file://0001-Update-to-p8-platform.patch \
          "

inherit cmake pkgconfig gettext

S = "${WORKDIR}/pvr.hts"

EXTRA_OECMAKE = "\
	  -DADDONS_TO_BUILD=pvr.hts \
	  -DADDON_SRC_PREFIX=${WORKDIR}/git \
	  -DCMAKE_BUILD_TYPE=Debug \
	  -DCMAKE_INSTALL_PREFIX=${datadir}/kodi/addons \
          -DCMAKE_MODULE_PATH=${STAGING_LIBDIR}/kodi \
	  -DCMAKE_PREFIX_PATH=${prefix} \
          -DPACKAGE_ZIP=1 \
        "

do_compile_prepend() {
	sed -i -e 's:I/usr/include:I${STAGING_INCDIR}:g' \
	       -e 's:-pipe:${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -pipe:g' \
	          ${B}/CMakeFiles/*/flags.make
	sed -i -e 's:-pipe:${HOST_CC_ARCH} ${TOOLCHAIN_OPTIONS} -pipe:g'\
	          ${B}/CMakeFiles/*/link.txt
}

INSANE_SKIP_${PN} = "dev-so"
FILES_${PN} += "${datadir}/kodi"
FILES_${PN}-dbg += "${datadir}/kodi/addons/*/.debug/"
