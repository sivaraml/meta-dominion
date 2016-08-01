SUMMARY = "Domoticz is a Home Automation system design to control various devices and receive input from various sensors. "

LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://License.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "lua sqlite3 boost curl openssl libusb zlib"

inherit cmake pkgconfig useradd systemd

PV = "3.5382+git${SRCPV}"

SRCREV = "7071bfc5a3642ae9fef7b0db43037187006f21a9"
SRC_URI = "git://github.com/domoticz/domoticz.git;protocol=https \
           file://hack.patch \
           file://domoticz.service \
          "

S = "${WORKDIR}/git"

EXTRA_OECMAKE = " -DBOOST_INCLUDEDIR=${STAGING_INCDIR} \
                  -DOPENSSL_INCLUDE_DIR=${STAGING_INCDIR} \
                  -DOPENSSL_LIBRARIES=${STAGING_LIBDIR} \
                  -DCURL_LIBRARIES=${STAGING_LIBDIR} \
                  -DCURL_INCLUDE_DIR=${STAGING_INCDIR} \
                "


do_install_append() {
    # The domoticz manual says "run from git checkout", but we don't tolerate such nonsense
    # and since 'make install' doesn't work properly, we do some massaging.
    install -d ${D}/foo
    mv ${D}${prefix}/* ${D}/foo
    install -d ${D}${localstatedir}/lib/domoticz
    mv ${D}/foo/* ${D}${localstatedir}/lib/domoticz
    rmdir ${D}/foo

    chown -R domoticz ${D}${localstatedir}/lib

    install -d ${D}${systemd_unitdir}/system
    sed s:LIBDIR:${localstatedir}/lib:g ${WORKDIR}/domoticz.service > ${D}${systemd_unitdir}/system/domoticz.service

}

FILES_${PN}-dbg += "${localstatedir}/lib/domoticz/.debug/"

SYSTEMD_SERVICE_${PN} = "domoticz.service"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = " \
    --system --no-create-home \
    --home ${localstatedir}/lib/domoticz \
    --groups dialout \
    --user-group domoticz"

# Domoticz is mostly used in combination with a smart meter (ftdi dongles) or an rftrxx (acm based).
RRECOMMENDS_${PN} += "kernel-module-cdc-acm \
                      kernel-module-usbserial \
                     "