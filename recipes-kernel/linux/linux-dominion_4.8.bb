require linux.inc

DEPENDS += "openssl-native"

DESCRIPTION = "Linux kernel"
KERNEL_IMAGETYPE ?= "zImage"

COMPATIBLE_MACHINE = "(dominion-old|dominion|beast|macbook|soekris-net6501|arietta-g25|macbook|minnow|minnowboard|fri2|beaglebone|apu2c4|revo|dlink-dns320)"

FILESPATH =. "${FILE_DIRNAME}/linux-dominion-4.8:${FILE_DIRNAME}/linux-dominion-4.8/${MACHINE}:"

S = "${WORKDIR}/git"

PV = "4.8.13"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-4.8.y"
SRCREV_pn-${PN} = "55d64c0b3c15cb5fb4d5a5bdebde9522e4ed52ff"

SRC_URI += " \
             file://0001-wireless-populate-db.txt.patch \
             file://0002-btusb-add-USB-ID-for-ASUS-X79-DELUXE-onboard-BT.patch \
             file://0003-Acme-boards-patches.patch \
             file://0004-ARM-DTS-arietta-G25-ttyS2-SPI0-ADC-on-expansion-head.patch \
             file://0005-ARM-DTS-acme-arietta-use-button-as-power-button.patch \
             file://0006-ARM-DTS-acme-arietta-drop-chosen-node.patch \
             file://0001-block-cgroups-kconfig-build-bits-for-BFQ-v7r11-4.7.0.patch \
             file://0002-block-introduce-the-BFQ-v7r11-I-O-sched-for-4.7.0.patch \
             file://0003-block-bfq-add-Early-Queue-Merge-EQM-to-BFQ-v7r11-for.patch \
             file://0004-block-bfq-turn-BFQ-v7r11-for-4.7.0-into-BFQ-v8r2-for.patch \
             file://am335x-pm-firmware.elf \
             file://defconfig \
           "

do_configure_prepend() {
	if [ -e ${WORKDIR}/am335x-pm-firmware.elf ] ; then
		cp ${WORKDIR}/am335x-pm-firmware.elf ${S}/firmware/
	fi
}
