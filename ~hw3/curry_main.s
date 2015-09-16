####################################################
#                    WARNING                       #
####################################################
# You may modify this file, but we will overwrite  #
# it with our own copy when grading.               #
####################################################
	.data
comma:
	.asciiz ", "
newline:
	.asciiz "\n"

        .text
#Tests that your code behaves as expected. If you're getting
#a bunch of comma separated 42's then you're passing these tests.
main:
        #test 1 argument case
        la $a0, destination
        la $a1, foo1
        li $a2, 1
        jal curry

        li $a0, 21
        jal destination
        move $a0, $v0
        li $v0 1
        syscall

        la $a0 comma
        li $v0 4
        syscall

        li $a0, 21
        jal foo1
        move $a0, $v0
        li $v0 1
        syscall

        la $a0 newline
        li $v0 4
        syscall

        #test 2 argument case
        la $a0, destination
        la $a1, foo2
        li $a2, 2
        jal curry

        li $a0, 6
        jal destination
        li $a0, 7
        jalr $v0
        move $a0, $v0
        li $v0 1
        syscall

        la $a0 comma
        li $v0 4
        syscall

        li $a0, 6
        li $a1, 7
        jal foo2
        move $a0, $v0
        li $v0 1
        syscall

        la $a0 newline
        li $v0 4
        syscall

        #test 3 argument case
        la $a0, destination
        la $a1, foo3
        li $a2, 3
        jal curry

        li $a0, 2
        jal destination
        li $a0, 3
        jalr $v0
        li $a0, 7
        jalr $v0
        move $a0, $v0
        li $v0 1
        syscall

        la $a0 comma
        li $v0 4
        syscall

        li $a0, 2
        li $a1, 3
        li $a2, 7
        jal foo3
        move $a0, $v0
        li $v0 1
        syscall

        la $a0 newline
        li $v0 4
        syscall

        #test 4 argument case
        la $a0, destination
        la $a1, foo4
        li $a2, 4
        jal curry

        li $a0, 4
        jal destination
        li $a0, 3
        jalr $v0
        li $a0, 6
        jalr $v0
        li $a0, 6
        jalr $v0
        move $a0, $v1
        li $v0 1
        syscall

        la $a0 comma
        li $v0 4
        syscall

        li $a0, 4
        li $a1, 3
        li $a2, 15
        li $a3, 6
        jal foo4
        move $a0, $v0
        li $v0 1
        syscall

        la $a0 newline
        li $v0 4
        syscall

        li $v0, 10
        syscall

#For fun exercise! Figure out what each of the foo functions does.
foo1:
	addu $v0, $a0, $a0
	jr $ra

foo2:
	and $v0, $0, $0
	slt $t0, $a1, $0
	beq $t0, $0, foo2_loop
	subu $a0, $0, $a0
	subu $a1, $0, $a1
foo2_loop:
	beq $a1, $0, end_foo2
	addu $v0, $v0, $a0
	addiu $a1, $a1, -1
	j foo2_loop
end_foo2:
	jr $ra

foo3:
	addiu $sp, $sp, -8
	sw $ra, 0($sp)
	sw $a2, 4($sp)
	jal foo2
	move $a0, $v0
	lw $a1, 4($sp)
	jal foo2
	lw $ra, 0($sp)
	addiu $sp, $sp, 8
	jr $ra

foo4:
	addiu $sp, $sp, -28
	sw $ra, 0($sp)
	sw $s0, 4($sp)
	sw $s1, 8($sp)
	sw $s2, 12($sp)
	sw $s3, 16($sp)
	sw $s4, 20($sp)
	sw $s5, 24($sp)

	move $s0, $a0
	move $s1, $a1
	move $s2, $a2
	move $s3, $a3

	move $a0, $s0
	move $a1, $s2
	jal foo2
	move $s4, $v0

	move $a0, $s1
	move $a1, $s3
	jal foo2
	subu $s4, $s4, $v0

	move $a0, $s0
	move $a1, $s3
	jal foo2
	move $s5, $v0

	move $a0, $s1
	move $a1, $s2
	jal foo2
	addu $s5, $s5, $v0

	move $v0, $s4
	move $v1, $s5

	lw $ra, 0($sp)
	lw $s0, 4($sp)
	lw $s1, 8($sp)
	lw $s2, 12($sp)
	lw $s3, 16($sp)
	lw $s4, 20($sp)
	lw $s5, 24($sp)
        addiu $sp, $sp, 28
        jr $ra

      	.include "curry.s"
