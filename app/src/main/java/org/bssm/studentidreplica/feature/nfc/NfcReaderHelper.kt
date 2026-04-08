package org.bssm.studentidreplica.feature.nfc

import android.nfc.Tag
import android.nfc.tech.NfcA
import javax.inject.Inject
import org.bssm.studentidreplica.core.model.TagInfo
import org.bssm.studentidreplica.core.util.HexFormatter
import org.bssm.studentidreplica.core.util.TagTypeDescriber

class NfcReaderHelper @Inject constructor() {
    fun extractPublicMetadata(tag: Tag, scannedAt: Long): TagInfo {
        val techList = tag.techList.toList()
        val nfca = requireNotNull(NfcA.get(tag)) {
            "이 앱은 NfcA 공개 메타데이터만 지원합니다."
        }
        val uid = HexFormatter.toUidString(tag.id)
        val atqa = HexFormatter.toShortHex(nfca.atqa)
        val sak = HexFormatter.toShortHex(nfca.sak.toShort())
        return TagInfo(
            uid = uid,
            tagType = TagTypeDescriber.describe(techList),
            techList = techList,
            atqa = atqa,
            sak = sak,
            scannedAt = scannedAt,
        )
    }
}
