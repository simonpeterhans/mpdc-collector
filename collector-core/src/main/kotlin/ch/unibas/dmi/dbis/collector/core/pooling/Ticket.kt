package ch.unibas.dmi.dbis.collector.core.pooling

class Ticket private constructor(val id: Long) {

    companion object {

        private var id: Long = -1

        private fun resetId() {
            id = -1
        }

        @Synchronized
        fun create(): Ticket {
            val ticket = Ticket(++id)

            if (id == Long.MAX_VALUE) {
                resetId()
            }

            return ticket
        }

    }

}
