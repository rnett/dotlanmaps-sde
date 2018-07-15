package com.rnett.ligraph.eve.dotlanmaps

import com.rnett.eve.ligraph.sde.*
import org.jetbrains.exposed.sql.transactions.transaction

val DotlanSystem.system get() = mapsolarsystems.fromID(this.systemID)
val DotlanSystem.constellation get() = mapconstellations.fromID(this.constellationID)
val DotlanSystem.region get() = mapregions.fromID(this.regionID)

val DotlanRegion.region get() = mapregions.fromID(this.regionID)

val DotlanJump.startSystem get() = mapsolarsystems.fromID(this.startSystemID)
val DotlanJump.endSystem get() = mapsolarsystems.fromID(this.endSystemID)
val DotlanJump.jump
    get() = transaction {
        mapsolarsystemjumps.findFromPKs(startSystemID, endSystemID)
                ?: mapsolarsystemjumps.findFromPKs(endSystemID, startSystemID)!!
    }

val mapregion.dotlanMap get() = DotlanMaps[regionID]