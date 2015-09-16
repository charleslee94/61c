# CS61C Sp14 Project 1-2
# Task A: The Collatz conjecture

.globl main
.include "collatz_common.s"

main:
	jal read_input			# Get the number we wish to try the Collatz conjecture on
	move $a0, $v0			# Set $a0 to the value read
	la $a1, collatz_recursive	# Set $a1 as ptr to the function we want to execute
	jal execute_function		# Execute the function
	li $v0, 10			# Exit
	syscall
	
# --------------------- DO NOT MODIFY ANYTHING ABOVE THIS POINT ---------------------

# Returns the stopping time of the Collatz function (the number of steps taken till the number reaches one)
# using an RECURSIVE approach. This means that if the input is 1, your function should return 0.
#
# The current value is stored in $a0, and you may assume that it is a positive number.
#
# Make sure to follow all function call conventions.
collatz_recursive:
	addi $sp $sp -12
	sw $ra 8($sp)
	sw $s0 4($sp)
	add $v0 $0 $0
	beq $a0 1 fin
	add $t0 $a0 0
	andi $t0 1
	bne $t0 0 odd
	sra $a0 $a0 1
	jal collatz_recursive
	add $s0 $v0 $0
	j next
odd:
	mul $a0 $a0 3
	addi $a0 $a0 1
	jal collatz_recursive
	add $s0 $v0 0
	j next
next:
	add $v0 $s0 1
fin:
	lw $s0 4($sp)
	lw $ra 8($sp)
	addi $sp $sp 12
	jr $ra
			
