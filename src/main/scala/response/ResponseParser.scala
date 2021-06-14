package response

trait ResponseParser[I, V] {

    def parse(identifier:I):V
}
