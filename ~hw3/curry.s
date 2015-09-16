.text
helper:
	addi $sp $sp -12
	sw $a0 12($sp)
	sw $a1 8($sp)
	sw $a2 4($sp)
	beqz $a2 done
	addi $a2 $a2 -1
	sw
	jal curry
# Your code here. Remember that you need to turn on self-modifying mode in order for
# Mars to allow self-modifying mips.
# $a0 corresponds to destination buffer for the curryable-ized function
# $a1 corresponds to the function to be curryable-ized
# $a2 corresponds to the number of arguments to the function to be curryable-ized
curry:
	jr $ra

done:
	lw $a0 12($sp)
	lw $a1 8($sp)
	lw $a2 4($sp)
	jr $ra
#This is the destination buffer we'll be using for your curryable-ized function.
#I found it helpful to scratch out what lines of code I'd be having my code
#write to this buffer before writing curry. Feel free to add more nops to this
#buffer if you need to, but know that your code should only need O(1) space for
#any function we hand you. If you find yourself wanting more than constant space
#you're probably doing something wrong.
destination:
	nop #
	nop #
        nop #
        nop #
        nop #

	nop #
	nop #
        nop #
        nop #
        nop #

	nop #
	nop #
        nop #
        nop #
        nop #

	nop #
	nop #
        nop #
        nop #
        nop #

        nop #
        nop #
        nop #
        nop #
        nop #
        nop #
        nop #
        nop #

        nop #
        nop #
        nop #
        nop #
        nop #
