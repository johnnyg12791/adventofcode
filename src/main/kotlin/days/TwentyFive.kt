package days

class TwentyFive {

    fun executeA(): Long {
//        val pkey1 = 5764801L
//        val pkey2 = 17807724L

        val pkey1 = 3418282L
        val pkey2 = 8719412L

        val subject = 7L
        val mod = 20201227L

        val loopSize1 = determinLoopSize(pkey1, subject, mod)
        val loopSize2 = determinLoopSize(pkey2, subject, mod)
        //println(pkey1 * loopSize2)
        val encKey1 = runLoops(loopSize2, pkey1, mod)
        val encKey2 = runLoops(loopSize1, pkey2, mod)


        if (encKey1 == encKey2) return encKey1
        else return -1

    }

    fun runLoops(numLoops: Long, subject: Long, mod: Long): Long {
        var nextSubject = subject
        for (i in 1 until numLoops) {
            nextSubject = (subject * nextSubject) % mod
        }
        return nextSubject
    }


    fun determinLoopSize(pkey: Long, subject: Long, mod: Long): Long {
        var loopSize = 1L
        var nextSubject = subject
        while (true) {
            loopSize += 1
            nextSubject = (subject * nextSubject) % mod
            if (nextSubject == pkey) return loopSize
        }

    }

    fun executeB(): Long {
        return 0
    }




}