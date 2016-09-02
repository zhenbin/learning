# -*- coding: UTF-8 -*-

class link_node:
    def __init__(self, name=''):
        self.name = name  # 名称
        self.next = None  # 尺寸


class link_list:
    def __init__(self):
        self.head = None

    def pop(self):
        node = None
        if self.head is not None:
            node = self.head
            self.head = node.next
        return node


    def add_to_next(self, node):
        node.next = self.head
        self.head = node

    def reverse(self):
        new_list = link_list()
        while self.head is not None:
            pop_node = self.pop()
            new_list.add_to_next(pop_node)
        self.head = new_list.head


list = link_list()  # 定义结构对象
names = ['aa', 'bb', 'cc']
for name in names:
    node = link_node(name)
    list.add_to_next(node)

piv = list.head
while piv is not None:
    print piv.name
    piv = piv.next

print "\nafter reversing.\n"

list.reverse()
piv = list.head
while piv is not None:
    print piv.name
    piv = piv.next
