import io.harmor.msgpack.ByteArraySink
import io.harmor.msgpack.ByteArraySource
import io.harmor.msgpack.DefaultMessageUnpacker
import io.harmor.msgpack.DefaultMessagePacker
import io.kotest.core.spec.style.WordSpec
import utils.MessagePackerFactory
import utils.MessageUnpackerFactory

abstract class AbstractMessagePackTest : WordSpec() {

    protected open val packer: MessagePackerFactory = {
        DefaultMessagePacker(ByteArraySink())
    }

    protected open val unpacker: MessageUnpackerFactory = {
        DefaultMessageUnpacker(ByteArraySource(it))
    }
}
